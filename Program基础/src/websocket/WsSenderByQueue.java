
import okhttp3.WebSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 功能描述
 *
 * @since 2020-08-08
 */
class WsSenderByQueue {

    private volatile WebSocket mWebSocket = null;

    private ConcurrentLinkedQueue<WsMsgEntity> msgQueue = null;

    private Map<String, WsMsgEntity> msgMap = new ConcurrentHashMap<>();

    /**
     * true: 可以发送
     * false： 消息只入队，不发送
     */
    private AtomicBoolean senderFlag = new AtomicBoolean(false);

    private static final Object LOCK = new Object();

    private static final String TAG = WsSenderByQueue.class.getSimpleName();

    public WsSenderByQueue() {
        msgQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * 1、设置新的websocket对象
     * 2、清理旧的websocket消息
     * 3、阻塞“处理msg”的线程handler，需要调用release才能开始处理
     *
     * @param mWebSocket 新的websocket对象
     */
    public void setAndHoldWebSocket(WebSocket mWebSocket) {
        this.mWebSocket = mWebSocket;
        // 阻塞住处理逻辑，使其后面的消息进入队列而不处理
        senderFlag.compareAndSet(true, false);

        // 当新连接建立时，先清空队列里面的旧消息
        // TODO 有些消息应该清空，有些消息不应该清空
        if (mWebSocket == null) {
            msgQueue.clear();
        }
    }

    public WsMsgEntity sendMessage(String requestId, String type, String msg, boolean checkConnection,
        IWsMsgCallback callback) {

        if (checkConnection && !senderFlag.get()) {
            Logger.w(TAG, "current websocket state is not connected");
            return null;
        }

        WsMsgEntity entity = new WsMsgEntity(requestId, type, msg, callback);
        msgMap.put(entity.requestId, entity);
        boolean result = msgQueue.offer(entity);
        handleMessage();
        return entity;
    }

    /**
     * 释放掉：
     * 释放掉栅栏，开始处理队列里面的消息
     */
    public void release() {
        senderFlag.compareAndSet(false, true);
        handleMessage();
    }

    /**
     * 网络中断：阻塞发送队列
     */
    public void lock() {
        senderFlag.compareAndSet(true, false);
    }

    public WsMsgEntity getSentMsg(String requestId) {
        synchronized (LOCK) {
            if (msgMap.containsKey(requestId)) {
                return msgMap.get(requestId);
            }
            return null;
        }

    }

    public void removeSentMsg(String requestId) {
        synchronized (LOCK) {
            if (msgMap.containsKey(requestId)) {
                msgMap.remove(requestId);
            }
        }

    }

    /**
     * 执行发送逻辑
     * 防止对同一个对象多个线程去执行触发
     */
    private void handleMessage() {
        synchronized (LOCK) {
            if (mWebSocket == null) {
                Logger.w(TAG, "websocket is null...please set and hold ");
                return;
            }

            if (!senderFlag.get()) {
                Logger.i(TAG, "sender flag is : false. That means message is not allowed to send");
                return;
            }

            while (msgQueue.size() != 0) {
                WsMsgEntity entity = msgQueue.remove();
                // 正常处理
                boolean result = mWebSocket.send(entity.sentMsg);
                Logger.d(TAG, "ws send msg = [%s] result = [%s]", entity.sentMsg, result);
            }

        }

    }

    static class WsMsgEntity {
        public String requestId;

        public String type;

        public String sentMsg;

        public IWsMsgCallback callback;

        public Runnable timeoutRunnable = null;

        public WsMsgEntity() {
        }

        public WsMsgEntity(String requestId, String type, String sentMsg, IWsMsgCallback callback) {
            this.requestId = requestId;
            this.type = type;
            this.sentMsg = sentMsg;
            this.callback = callback;
        }

        public void startTiming(Runnable timeoutRunnable, long timeout) {
            this.timeoutRunnable = timeoutRunnable;
            WsThread.delayOnWorkerThread(timeoutRunnable, timeout);
        }

        public void stopTiming() {
            if (timeoutRunnable != null) {
                WsThread.removeDelayedTask(timeoutRunnable);
                timeoutRunnable = null;
            }
        }

    }

}
