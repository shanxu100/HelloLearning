
import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能描述
 *
 * @since 2020-12-25
 */
class WsThread {

    private static final String TAG = "WsThreadManager";

    /**
     * 线程池
     */
    private static volatile ScheduledThreadPoolExecutor mThreadPool = null;

    /**
     * 线程池维护线程的最少数量
     */
    private static final int SIZE_CORE_POOL = 1;

    /**
     * 线程池维护线程的最大数量
     */
    private static final int SIZE_MAX_POOL = 1;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private static final int TIME_KEEP_ALIVE = 5000;

    /**
     * 线程池名称
     */
    private final static String THREAD_POOL_NAME_PREFIX = "ws_tpool_";

    /**
     * 运行在sdk工作线程执行
     *
     * @param runnable runnable
     */
    public static void runOnWorkerThreadSafely(Runnable runnable) {
        if (runnable == null) {
            Logger.e(TAG, "runOnWorkerThreadSafely: runnable is null...can not run On Worker Thread");
            return;
        }
        try {
            getThreadPool().execute(runnable);
        } catch (Exception e) {
            Logger.e(TAG, "post to Worker Thread exception", e);
        }
    }

    /**
     * 延迟任务：运行在sdk工作线程执行
     *
     * @param runnable runnable
     * @param delayMillis delayMillis
     */
    public static void delayOnWorkerThread(Runnable runnable, long delayMillis) {
        if (runnable == null) {
            Logger.e(TAG, "scheduleOnWorkerThread: runnable is null...can not run On Worker Thread");
            return;
        }
        getThreadPool().schedule(runnable, delayMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 取消添加在工作线程上的延迟任务
     *
     * @param runnable runnable
     */
    public static void removeDelayedTask(Runnable runnable) {
        if (runnable == null) {
            Logger.e(TAG, "removeScheduleTask: runnable is null...can not run On Worker Thread");
            return;
        }
        getThreadPool().remove(runnable);
    }

    /**
     * 执行callable任务
     *
     * @param task task
     * @param <T> T
     * @return futureTask
     */
    public static <T> Future<T> runFutureTask(Callable<T> task) {
        if (task == null) {
            Logger.w(TAG, "Callable task is null....");
            return null;
        }
        return getThreadPool().submit(task);
    }

    /**
     * 执行 runnable 任务
     *
     * @param task runnable
     */
    public static void runTask(Runnable task) {
        if (task == null) {
            Logger.w(TAG, "Callable task is null....");
            return;
        }
        getThreadPool().execute(task);
    }

    /**
     * 执行周期任务：一个任务，两次执行间隔固定的时间
     * 在线程池中执行
     *
     * @param task 在线程池中执行的task
     * @param initialDelay 首次执行的延迟时间 ms
     * @param period 任务间的执行周期
     * @return 该任务的future，取消时需要调用 future.cancel
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period) {
        if (task == null) {
            Logger.w(TAG, "runnable task is null....");
            return null;
        }
        return getThreadPool().scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止所有线程
     */
    public static void stop() {
        if (mThreadPool != null) {
            mThreadPool.shutdown();
            mThreadPool = null;
        }
        Logger.i(TAG, "ThreadManager stopAll");
    }

    // ==========================================================================
    //
    // ==========================================================================

    /**
     * 获取线程池实例
     *
     * @return threadPool
     */
    private static ScheduledThreadPoolExecutor getThreadPool() {
        if (mThreadPool == null) {
            synchronized (WsThread.class) {
                if (mThreadPool == null) {
                    mThreadPool = new ScheduledThreadPoolExecutor(SIZE_CORE_POOL,
                        new NamedThreadFactory(THREAD_POOL_NAME_PREFIX));
                    mThreadPool.setMaximumPoolSize(SIZE_MAX_POOL);
                    mThreadPool.setKeepAliveTime(TIME_KEEP_ALIVE, TimeUnit.MILLISECONDS);
                    mThreadPool.allowCoreThreadTimeOut(true);
                }
            }
        }
        return mThreadPool;
    }

    /**
     * 对线程中的未捕获异常进行处理的Handler
     * 捕获顺序：
     * 1、线程中自己的 try-catch
     * 2、线程自己的 myThread.setUncaughtExceptionHandler
     * 3、默认的 Thread.setDefaultUncaughtExceptionHandler ，这里设置的handler由当前进程的所有线程共享。
     * <p>
     * 对于3，在sdk中慎用。因为上层业务也可能设置默认的handler，造成sdk和app相互覆盖自己handler。
     * 或者在设置之前先 Thread.getDefaultUncaughtExceptionHandler 判断其他业务有没有设置 默认的handler。
     */
    private static final class WorkerThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
            Logger.e(TAG, "Thread cause UncaughtException. ThreadId is " + t.getId() + ". ThreadName is " + t.getName(),
                e);
        }
    }

    /**
     * 线程工厂，负责创建线程池中的线程
     */
    private static class NamedThreadFactory implements ThreadFactory {
        /**
         * 线程计数，用于拼接线程名称
         */
        private final AtomicInteger mThreadNumber = new AtomicInteger(1);

        private final String mNamePrefix;

        NamedThreadFactory(String namePrefix) {
            mNamePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r, mNamePrefix + mThreadNumber.getAndIncrement());
            thread.setUncaughtExceptionHandler(new WorkerThreadExceptionHandler());
            return thread;
        }
    }

}
