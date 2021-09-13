
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 功能描述
 * TODO 如何定义 重连的场景
 *
 * @since 2020-08-06
 */
public class WsManager extends WebSocketListener implements IWsManager, WsReconnector.IReconnectCallback {

    private final static String TAG = WsManager.class.getSimpleName();

    private static final Object LOCK = new Object();

    private static WsManager instance = null;

    /**
     * sender 持有websocket对象。所以，WsManager销毁时，这个websocket对象也应该显式地销毁
     */
    private WebSocket mWebSocket = null;

    private WsConfig wsConfig = null;

    private IWsCallback wsCallback = null;

    private OkHttpClient httpClient = null;

    private WsSenderByQueue wsSender = null;

    private WsReconnector reconnector = null;

    private WsManager() {

        wsSender = new WsSenderByQueue();

        reconnector = new WsReconnector(this);
    }

    public static IWsManager getInstance() {
        if (instance == null) {
            synchronized (WsManager.class) {
                if (instance == null) {
                    instance = new WsManager();
                }
            }
        }
        return instance;
    }

    // ===========================================================
    //
    // ===========================================================

    // region websockt连接状态监听

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Logger.i(TAG, "client onOpen...");
        if (!isSameWebSocket(webSocket)) {
            Logger.w(TAG, "The webSocket on callback is not the same mWebSocket we expected... do nothing ... return");
            return;
        }
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                // 释放掉 wsSender 的栅栏，使其开始处理任务
                if (reconnector.isReconnecting()) {
                    reconnector.cancel();
                    callbackOnCode(WsStatus.CODE.NETWORK_RECONNECT_SUC, "websocket has been reconnected successfully");
                } else {
                    callbackOnCode(WsStatus.CODE.NETWORK_CONNECT_SUC, "websocket has been connected successfully");
                }
                wsSender.release();
            }
        });
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // Logger.d(TAG, "onMessage" + text);
        if (!isSameWebSocket(webSocket)) {
            Logger.w(TAG, "The webSocket on callback is not the same mWebSocket we expected... do nothing ... return");
            return;
        }
        handleMessage(text);

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Logger.w(TAG, "websocket receive byteString...do not know how to deal with the data");
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        // TODO 这条日志需要评审
        Logger.d(TAG, "onClosing code:[%s] reason:[%s]", code, reason);
        if (!isSameWebSocket(webSocket)) {
            Logger.w(TAG, "The webSocket on callback is not the same mWebSocket we expected... do nothing ... return");
            return;
        }
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                wsSender.setAndHoldWebSocket(null);
                callbackOnCode(code, reason);
                stopConnection();
            }
        });
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Logger.i(TAG, "client onClosed, code:" + code + "--reason:" + reason);
    }

    /**
     * 如果websocket调用了close或closing，那么再发消息就会 回调 onFailure。
     * 因为设置了 retryTimeOnConnectionFailure ，那么在此时就会重连（resclient sdk）
     * bug回顾：
     * 被T掉时回调了 onClosing 。此时退出房间，会发exitroom和bye两条消息给服务器。
     * 那么就会回调 onFailure ，并且 restclient自动触发重连。
     *
     * @param webSocket webSocket
     * @param throwable throwable
     * @param response response
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
        Logger.e(TAG, "client onFailure...", throwable);
        if (!isSameWebSocket(webSocket)) {
            Logger.w(TAG, "The webSocket on callback is not the same mWebSocket we expected... do nothing ... return");
            return;
        }
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                if (isAuthFailed(throwable, response)) {
                    reconnector.cancel();
                    callbackOnCode(WsStatus.CODE.AUTHORIZATION_ERROR, "AUTHORIZATION on Failure...");
                    return;
                }
                wsSender.lock();
                reconnector.tryAgain();
            }
        });
    }

    // endregion

    // ===========================================================
    //
    // ===========================================================

    /**
     * TODO 在worker线程执行
     */
    @Override
    public void startConnection(WsConfig cfg, IWsCallback callback) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                wsConfig = cfg;
                wsCallback = callback;
                startConnectInternal();
            }
        });

    }

    /**
     * TODO 在worker线程执行
     */
    @Override
    public void stopConnection() {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                stopConnectionInternal();
            }
        });

    }

    /**
     * TODO 在worker线程执行
     */
    @Override
    public void renewToken(String authorization) {
        wsConfig.authorization = authorization;
        Logger.i(TAG, "get new authorization to buildConnect");
        startConnectInternal();
    }

    /**
     * TODO 在worker线程执行
     */
    @Override
    public boolean sendMessage(String requestId, String type, String msg, boolean checkConnection,
        IWsMsgCallback callback) {

        if (TextUtils.isEmpty(msg)) {
            Logger.i(TAG, "can not send empty msg...return");
            return false;
        }

        WsSenderByQueue.WsMsgEntity entity = wsSender.sendMessage(requestId, type, msg, checkConnection, callback);
        if (entity == null) {
            callback.onTimeout(requestId, msg);
            return false;
        }
        // 设置超时计时，超时时，回调 onTimeout
        entity.startTiming(new Runnable() {
            @Override
            public void run() {
                wsSender.removeSentMsg(requestId);
                if (callback != null) {
                    callback.onTimeout(requestId, msg);
                }
            }
        }, 5000);
        return true;
    }

    // ==================================================================
    //
    // ==================================================================

    @Override
    public void onPerformReconnect(ReconnectionInfo info) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                // 当触发重连时，执行以下动作
                callbackOnCode(WsStatus.CODE.NETWORK_RECONNECTING, info.toJson());
                startConnectInternal();
            }
        });

    }

    @Override
    public void outOfReconnectTimeLimit() {
        Logger.w(TAG, "reconnect outOfReconnectTimeLimit");
        callbackOnCode(WsStatus.CODE.NETWORK_FAILURE, "NETWORK on Failure...");
        stopConnection();
    }

    // ==================================================================
    //
    // ==================================================================

    private void startConnectInternal() {

        if (wsConfig == null) {
            Logger.w(TAG, "cannot start websocket connection....wsConfig is null");
            callbackOnCode(WsStatus.CODE.INIT_WEB_SOCKET_FAILED, "init websocket failed... wsConfig is null");
            return;
        }
        try {
            Request mRequest = new Request.Builder().url(wsConfig.wsUrl)
                .addHeader("Authorization", wsConfig.authorization)
                .addHeader("roomId", wsConfig.roomId)
                .addHeader("userId", wsConfig.userId)
                .addHeader("appId", wsConfig.appId)
                .addHeader("traceId", wsConfig.traceId)
                .addHeader("clientVersion", wsConfig.clientVersion)
                .addHeader("romVersion", wsConfig.romVersion)
                .addHeader("devType", wsConfig.devType)
                .addHeader("netType", wsConfig.netType)
                .build();
            Logger.i(TAG, "[TimeCost][Step 2]build websocket, start time:" + System.currentTimeMillis());
            mWebSocket = getHttpClient().newWebSocket(mRequest, this);
            wsSender.setAndHoldWebSocket(mWebSocket);
        } catch (Exception e) {
            Logger.e(TAG, "initWebSocket Exception", e);
            callbackOnCode(WsStatus.CODE.INIT_WEB_SOCKET_FAILED, "initWebSocket Exception");
        }
    }

    private void stopConnectionInternal() {
        Logger.i(TAG, "WsManager stopConnection...");
        wsSender.setAndHoldWebSocket(null);
        reconnector.cancel();
        wsConfig = null;
        wsCallback = null;
        if (mWebSocket != null) {
            boolean result = mWebSocket.close(WsStatus.CODE.NORMAL_CLOSE, WsStatus.TIP.NORMAL_CLOSE);
            Logger.i(TAG, "stop connection internal....close websocket...result:" + result);
            mWebSocket = null;
        }
        if (httpClient != null) {
            httpClient.dispatcher().cancelAll();
            httpClient = null;
        }
        WsThread.stop();
    }

    private OkHttpClient getHttpClient() {
        synchronized (LOCK) {
            if (httpClient == null) {
                // ping 5秒与服务器对齐，否则容易心跳超时
                httpClient = new OkHttpClient.Builder()
                    // Context context
                    // .sslSocketFactory(SecureSSLSocketFactory.getInstance(context), new
                    // SecureX509TrustManager(context))
                    // .hostnameVerifier(new StrictHostnameVerifier())
                    // .connectTimeout(20, TimeUnit.SECONDS)
                    // .readTimeout(10, TimeUnit.SECONDS)
                    // .writeTimeout(10, TimeUnit.SECONDS)
                    // .callTimeout(5000)
                    // .pingInterval(5000)
                    .build();
            }
            return httpClient;
        }
    }

    /**
     * 把websocket的回调统一丢回给worker线程
     *
     * @param runnable runnable
     */
    private void runOnWorkerThread(Runnable runnable) {
        WsThread.runOnWorkerThreadSafely(runnable);
    }

    private void callbackOnCode(int code, String reason) {
        if (wsCallback == null) {
            Logger.i(TAG, "WsCallback is null");
            return;
        }
        wsCallback.onCode(code, reason);

    }

    /**
     * TODO 判断发生 onFailure的时候是否提示鉴权失败。
     * 具体业务，具体判断
     *
     * @param throwable throwable
     * @param response response
     * @return true：发生了鉴权失败 false:未发生鉴权失败
     */
    private boolean isAuthFailed(Throwable throwable, Response response) {

        // if (response != null && response.getCode() == 401) {
        // Logger.i(TAG, "isAuthFailed: true [case 1]");
        // return true;
        // }
        if (response == null && throwable != null) {
            String throwableMsg = throwable.getMessage();
            if (!isEmpty(throwableMsg) && throwableMsg.contains("401 Unauthorized")) {
                Logger.i(TAG, "isAuthFailed: true [case 2]");
                return true;
            }
        }
        return false;

    }

    /**
     * 判断回调的 WebSocket 和 本地自己使用的 WebSocket 是否是同一个对象
     *
     * @param targetWs targetWs
     * @return true 同一个对象，false 不同的对象
     */
    private boolean isSameWebSocket(WebSocket targetWs) {
        if (mWebSocket == null || targetWs == null) {
            return false;
        }
        // return mWebSocket.hashCode() == targetWs.hashCode();
        return mWebSocket == targetWs;
    }

    public static boolean isEmpty(String text) {
        if (text != null && !text.trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static JsonObject getJsonObj(String msgContent) {
        JsonObject jsonObject = null;
        if (isEmpty(msgContent)) {
            return jsonObject;
        }
        try {
            jsonObject = new JsonParser().parse(msgContent).getAsJsonObject();
        } catch (JsonParseException e) {
            Logger.e(TAG, "json paras error:", e);
        }
        return jsonObject;
    }

    private void handleMessage(String receivedMsgText) {
        if (TextUtils.isEmpty(receivedMsgText)) {
            Logger.w(TAG, "msgText is empty");
            return;
        }

        // Logger.e(TAG, "onMessage = " + receivedMsgText);

        JsonObject jsonObject = getJsonObj(receivedMsgText);
        // 云侧主动推送或广播事件没有错误返回
        if (jsonObject == null) {
            Logger.w(TAG, "msgText --> getJsonObj = null....return");
            return;
        }
        if (!jsonObject.has("requestId")) {
            Logger.w(TAG, "onMessage handleMessageByRequestId : requestId is empty");
            return;
        }

        String requestId = jsonObject.get("requestId").getAsString();

        WsSenderByQueue.WsMsgEntity entity = wsSender.getSentMsg(requestId);
        wsSender.removeSentMsg(requestId);
        if (entity != null) {
            // 停止超时计时
            entity.stopTiming();
            if (entity.callback == null) {
                Logger.i(TAG, "ignore current msg : callback is null requestId" + requestId);
                return;
            }
            entity.callback.onMessage(entity.requestId, entity.type, entity.sentMsg, receivedMsgText);
            return;
        }
        wsCallback.onPush(receivedMsgText);

    }

}
