

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述
 *
 * @since 2020-08-08
 */
class WsReconnector {

    private static final String TAG = WsReconnector.class.getSimpleName();

    private Future<Void> future = null;

    private IReconnectCallback callback = null;

    private Strategy strategy = null;

    private Runnable outOfTimeListener = new Runnable() {
        @Override
        public void run() {
            if (callback != null) {
                Logger.w(TAG, "reconnect timeout: from outOfTimeListener");
                callback.outOfReconnectTimeLimit();
            }
        }
    };

    /**
     * 重连总时间
     */
    private static final int TOTAL_CONNECT_TIME = 50000;

    /**
     * 重连等待时间
     */
    private static final int WAITING_TIME = 1000;

    public WsReconnector(IReconnectCallback callback) {
        this.callback = callback;
        strategy = Strategy.newDefaultStrategy();
    }

    /**
     * 仅尝试一次：
     * 即消耗strategy的一个entry，并执行一次onPerformReconnect
     * 注意，服务器目前心跳超时，等待30s再清用户数据，
     * 客户端最大重连时间计算：首次等待time+connectTimeout(3s)+(reconnectTime+connectTimeout)*重连次数-1
     */
    public void tryAgain() {

        if (future != null && !future.isDone()) {
            Logger.i(TAG, "trigger reconnect....operate frequently...ignore");
            return;
        }
        Logger.i(TAG, "trigger reconnect....");

        Strategy.Entry entry = strategy.next();
        if (entry == null) {
            Logger.w(TAG, "reconnect timeout: entry == null");
            callback.outOfReconnectTimeLimit();
            return;
        }
        if (entry.isInitialEntry()) {
            // 增加“超时回调”的延迟任务，在worker线程执行
            WsThread.delayOnWorkerThread(outOfTimeListener, TOTAL_CONNECT_TIME);
        }

        Callable<Void> task = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    Logger.i(TAG, "try-again   count: [%d]    wait time: [%d]", entry.count, entry.time);
                    // 先阻塞 time 长的时间，然后再执行重连
                    CountDownLatch latch = new CountDownLatch(1);
                    latch.await(entry.time, TimeUnit.MILLISECONDS);
                    callback.onPerformReconnect(new ReconnectionInfo(entry.count, entry.startConnectTime));
                    Logger.i(TAG, "try-again   count: [%d]   finish... ", entry.count);
                } catch (InterruptedException e) {
                    Logger.w(TAG, "try-again   sleeping-thread is interrupted...", e);
                }
                return null;
            }
        };
        future = WsThread.runFutureTask(task);
    }

    /**
     * 取消/中断 重连
     */
    public void cancel() {
        // 重新创建策略对象，，参数会重置
        strategy = Strategy.newDefaultStrategy();
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        future = null;
        WsThread.removeDelayedTask(outOfTimeListener);
    }

    /**
     * 判断当前是否正在重连
     *
     * @return true: 正在重连 false：未出去重连状态，比如从未执行过重连或者已经取消过重连
     */
    public boolean isReconnecting() {
        return future != null;
    }

    public interface IReconnectCallback {

        /**
         * 开始执行reconnect动作
         * 即，当触发重连时，执行以下动作
         * 在子线程（非工作线程）返回该回调
         *
         * @param info info
         */
        void onPerformReconnect(ReconnectionInfo info);

        /**
         * 当重试次数超过限制时，
         */
        void outOfReconnectTimeLimit();

    }

    /**
     * 生成重连策略：如
     */
    private static final class Strategy {

        private Entry entry = null;

        private Strategy() {

        }

        /**
         * 获取一个默认的初始化策略
         *
         * @return Strategy
         */
        public static Strategy newDefaultStrategy() {
            return new Strategy();
        }

        /**
         * 获取下一个重试策略 entry
         * 其中，entry的count是从0开始，逐渐累加的
         *
         * @return entry
         */
        public Entry next() {
            // 记录刚开始重连时间
            if (entry == null) {
                entry = new Entry(0, WAITING_TIME, System.currentTimeMillis());
            } else {
                entry.count = entry.count + 1;
            }
            // 超过重连时间重连结束
            long currentCastTime = System.currentTimeMillis() - entry.startConnectTime;
            Logger.d(TAG, "reconnect,currentCastTime:" + currentCastTime);
            if (currentCastTime >= TOTAL_CONNECT_TIME) {
                return null;
            }
            return entry;
        }

        private static final class Entry {
            int count = 0;

            int time = 0;

            long startConnectTime = 0;

            public Entry(int count, int time, long startConnectTime) {
                this.count = count;
                this.time = time;
                this.startConnectTime = startConnectTime;
            }

            public boolean isInitialEntry() {
                return this.count == 0;
            }

        }

    }

}
