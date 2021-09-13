

import android.text.TextUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 功能描述
 *
 * @since 2020-08-07
 */
class WsSenderNo {

    private WebSocket mWebSocket = null;

    private Callable<Boolean> sendCallable = null;

    private Future<Boolean> sendMsgFuture = null;

    private static final String TAG = WsSenderNo.class.getSimpleName();

    /**
     * 发送的消息队列
     */
    private BlockingQueue<String> sendMsgQueue = null;

    private static final class InstanceHolder {
        private static WsSenderNo instance = new WsSenderNo();
    }

    public static WsSenderNo getInstance() {
        return InstanceHolder.instance;
    }

    private WsSenderNo() {

    }

    public void init(WebSocket mWebSocket) {
        this.mWebSocket = mWebSocket;
        this.sendMsgQueue = new LinkedBlockingQueue<>();

        // 初始化 发送队列 和 callable
        this.sendCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return initInternal();
            }
        };
    }

    /**
     * 初始化 发送任务callable
     *
     * @return true: 发送任务task正常执行结束 false：发送任务task在执行过程中发生异常并
     */
    private boolean initInternal() {
        try {
            String msg;
            while (!TextUtils.isEmpty(msg = sendMsgQueue.take())) {

                try {
                    // 防止因为 websocket 异常导致整个task的执行
                    boolean result = mWebSocket.send(msg);
                    Logger.i(TAG, "send websocket msg result:" + result);
                    Logger.d(TAG, "send websocket msg is :" + msg);
                } catch (Exception e) {
                    Logger.e(TAG, "websocket send exception", e);
                }

            }
            return true;
        } catch (InterruptedException e) {
            // 执行take阻塞的时候发生了线程中断，如线程池shutdownNow或future.cancel()
            // 此时需要结束task执行，所以这个exception是正常现象
            Logger.w(TAG, "sender task is interrupted...");
            return true;
        }
    }

    /**
     * 入队，准备发送
     * TODO 防止历史消息堆积到队列，超时的消息需要从队列里面删除
     *
     * @param msg msg
     */
    public void offer(String msg) {
        if (sendMsgQueue != null) {
            sendMsgQueue.offer(msg);
        }
    }

    /**
     * 开启子线程发送数据
     */
    public void start() {
        if (sendMsgFuture != null && !sendMsgFuture.isDone()) {
            Logger.w(TAG, "sender future task has benn already created...do nothing return");
            return;
        }

        sendMsgFuture = ThreadManger.runFutureTask(sendCallable);

    }

    /**
     * 关闭子线程，停止发送数据
     */
    public void stop() {

        if (sendMsgFuture != null && !sendMsgFuture.isDone()) {
            sendMsgFuture.cancel(true);
        }
        sendMsgQueue.clear();
        sendMsgQueue = null;
        sendMsgFuture = null;

        Logger.i(TAG, "stop websocket sender...");

    }

}
