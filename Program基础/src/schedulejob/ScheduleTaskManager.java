
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能描述
 *
 * @since 2020-12-22
 */
public class ScheduleTaskManager {

    /**
     * 线程池维护线程的最少数量
     */
    private static final int SIZE_CORE_POOL = 1;

    /**
     * 线程池维护线程的最大数量
     */
    private static final int SIZE_MAX_POOL = 1;

    /**
     * 线程池维护线程所允许的空闲时间 s秒
     */
    private static final int TIME_KEEP_ALIVE = 5000;

    /**
     * 线程池名称
     */
    private final static String NAME_PREFIX = "sche_pool";


    private ScheduledThreadPoolExecutor executor = null;


    private static ScheduleTaskManager getInstance() {
        return InstanceHolder.INSTANCE;
    }


    private static final class InstanceHolder {
        private static final ScheduleTaskManager INSTANCE = new ScheduleTaskManager();
    }


    private ScheduleTaskManager() {
        executor =
                new ScheduledThreadPoolExecutor(SIZE_CORE_POOL, new ScheduleTaskThreadFactory(NAME_PREFIX));
        executor.setMaximumPoolSize(SIZE_MAX_POOL);
        executor.setKeepAliveTime(TIME_KEEP_ALIVE, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
    }


    /**
     * 延迟指定时间执行
     *
     * @param task  task
     * @param delay 延迟
     * @param unit  时间单位
     * @return futureTask：取消执行时，调用cancel方法
     */
    public static ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        if (task == null) {
            System.out.println("task is null");
            return null;
        }
        return getInstance().executor.schedule(task, delay, unit);
    }


    /**
     * 执行周期性的任务：
     * task 第一次执行和第二次执行的间隔 固定时间
     *
     * @param task         task
     * @param initialDelay 首次执行的延迟时间 ms
     * @param period       任务间的执行周期
     * @return futureTask：取消执行时，调用cancel方法
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period) {
        if (task == null) {
            System.out.println("scheduleAtFixedRate: runnable is null");
            return null;
        }
        return getInstance().executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);

    }

    /**
     * 执行固定间隔的任务：
     * task 上次执行完成的结束时间与本次执行的开始时间的固定间隔
     *
     * @param task         task
     * @param initialDelay 首次执行的延迟时间 ms
     * @param delay        任务上次执行完成的结束时间与本次执行的开始时间的间隔
     * @return futureTask：取消执行时，调用cancel方法
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay) {
        if (task == null) {
            System.out.println("scheduleWithFixedDelay: runnable is null");
            return null;
        }
        return getInstance().executor.scheduleWithFixedDelay(task, initialDelay, delay, TimeUnit.MILLISECONDS);

    }


    /**
     * ThreadFactory:指定线程名称
     */
    private static final class ScheduleTaskThreadFactory implements ThreadFactory {
        private final AtomicInteger mThreadNumber = new AtomicInteger(1);

        private final String mNamePrefix;

        public ScheduleTaskThreadFactory(String namePrefix) {
            mNamePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r, mNamePrefix + mThreadNumber.getAndIncrement());
            thread.setUncaughtExceptionHandler(new ScheduleTaskExceptionHandler());
            return thread;
        }
    }


    /**
     * 捕获线程未catch的异常
     */
    private static final class ScheduleTaskExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Thread cause UncaughtException. ThreadId is " + t.getId() + ". ThreadName is " + t.getName());
            e.printStackTrace();
        }
    }


}
