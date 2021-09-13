
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程集中控制： 调用本类分配的线程，都是有名字、有上限数量控制的
 *
 * @since 2019-09-23
 */
public class ExecutorsUtil {
    private static final String TAG = "ExecutorsUtil";

    private static final long DEFAULT_KEEP_ALIVE_TIME = 60L;

    private static final String THREAD_POOL_PREFIX_MESSAGE = "Core-Message"; // message线程池前缀,0~5
    private static final int MESSAGE_THREAD_MAX = 5;

    private static final String THREAD_POOL_PREFIX_DEFAULT = "Core-Task"; // core的默认线程池前缀, 0~10
    private static final int DEFAULT_THREAD_MAX = 10;

    private static final String THREAD_POOL_PREFIX_LOG = "Core-Log"; // core的日志线程池前缀, 1
    private static final String THREAD_POOL_PREFIX_PING = "Core-Ping"; // ping的线程池前缀,0~1
    private static final String THREAD_POOL_PREFIX_TRACKER = "Core-Tracker"; // 报表的线程池前缀,1

    private static final String THREAD_POOL_PREFIX_AUTH_SERVICE = "Core-AuthSrv"; // AuthService线程池前缀, 0~5
    private static final int AUTH_SERVICE_THREAD_MAX = 5;

    private static final String THREAD_POOL_PREFIX_CONNECT = "Core-Connect"; // Connect线程池前缀, 0~10
    private static final int CONNECT_THREAD_MIN = 1;
    private static final int CONNECT_THREAD_MAX = 10;

    private static final String THREAD_POOL_PREFIX_GWTASK = "Core-GWTask"; // GWTask线程池前缀, 0~5
    private static final int GWTASK_THREAD_MAX = 5;

    private static final String THREAD_POOL_PREFIX_GET_SCOPE = "Core-Scope"; // core的默认线程池前缀, 0~10

    private static final int CONFIG_SIG_THREAD_MAX = 5; // KitService中ConfigSignature线程池
    private static final String THREAD_POOL_PREFIX_CONFIG_SIG = "Config-Sig-Task";

    private static final int VERSION_TABLE_THREAD_MAX = 5; // KitService中versionTable线程池
    private static final String THREAD_POOL_PREFIX_VERSION_TAB = "Version-Tab-Task";

    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(1);
        private final String poolName;

        /**
         * 构造
         *
         * @param poolName 线程池名字
         */
        NamedThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, poolName + "-" + counter.getAndIncrement());
        }
    }

    private static ExecutorService defaultThreadPool;
    private static ExecutorService logThreadPool;
    private static ExecutorService pingThreadPool;
    private static ExecutorService trackerThreadPool;
    private static ExecutorService authServiceThreadPool;
    private static ExecutorService connectThreadPool;
    private static ExecutorService messageThreadPool;
    private static ExecutorService getScopeThreadPool;
    private static ExecutorService configSignaturePool;
    private static ExecutorService versionTablePool;

    /**
     * 获取默认线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getDefaultThreadPool() {
        if (defaultThreadPool == null) {
            defaultThreadPool = newCachedThreadPool(0, DEFAULT_THREAD_MAX, THREAD_POOL_PREFIX_DEFAULT);
        }
        return defaultThreadPool;
    }

    /**
     * 获取日志线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getLogThreadExecutor() {
        if (logThreadPool == null) {
            logThreadPool = newSingleThreadExecutor(THREAD_POOL_PREFIX_LOG);
        }
        return logThreadPool;
    }

    /**
     * 获取ping任务线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getPingThreadExecutor() {
        if (pingThreadPool == null) {
            pingThreadPool = newCachedThreadPool(0, 1, THREAD_POOL_PREFIX_PING);
        }
        return pingThreadPool;
    }


    /**
     * 获取tracker任务线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getTrackerThreadExecutor() {
        if (trackerThreadPool == null) {
            trackerThreadPool = newSingleThreadExecutor(THREAD_POOL_PREFIX_TRACKER);
        }
        return trackerThreadPool;
    }

    /**
     * 获取AuthService线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getAuthServiceThreadExecutor() {
        if (authServiceThreadPool == null) {
            authServiceThreadPool = newCachedThreadPool(0, AUTH_SERVICE_THREAD_MAX, THREAD_POOL_PREFIX_AUTH_SERVICE);
        }
        return authServiceThreadPool;
    }

    /**
     * 获取Connect线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getConnectThreadExecutor() {
        if (connectThreadPool == null) {
            connectThreadPool = newCachedThreadPool(CONNECT_THREAD_MIN, CONNECT_THREAD_MAX, THREAD_POOL_PREFIX_CONNECT);
        }
        return connectThreadPool;
    }

    /**
     * 获取Connect线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getGWTaskThreadExecutor() {
        if (connectThreadPool == null) {
            connectThreadPool = newCachedThreadPool(0, GWTASK_THREAD_MAX, THREAD_POOL_PREFIX_GWTASK);
        }
        return connectThreadPool;
    }

    /**
     * 获取Message线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getMessageThreadExecutor() {
        if (messageThreadPool == null) {
            messageThreadPool = newCachedThreadPool(0, MESSAGE_THREAD_MAX, THREAD_POOL_PREFIX_MESSAGE);
        }
        return messageThreadPool;
    }

    /**
     * 获取日志线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getScopeThreadExecutor() {
        if (getScopeThreadPool == null) {
            getScopeThreadPool = newCachedThreadPool(0, DEFAULT_THREAD_MAX, THREAD_POOL_PREFIX_GET_SCOPE);
        }
        return getScopeThreadPool;
    }

    private static ExecutorService newCachedThreadPool(int corePoolSize, int maximumPoolSize, String poolName) {
        return newCachedThreadPool(corePoolSize, maximumPoolSize, new NamedThreadFactory(poolName));
    }

    // 注意SynchronousQueue不缓存，如果线程都占用，新任务默认策略是抛出异常
    private static ExecutorService newCachedThreadPool(int corePoolSize,
                                                        int maximumPoolSize, ThreadFactory threadFactory) {
        // 使用了LinkedBlockingQueue，并发线程数是第一个参数corePoolSize
        ThreadPoolExecutor executor = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize,
                DEFAULT_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);

        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    private static ExecutorService newSingleThreadExecutor(String poolName) {
        return Executors.newSingleThreadExecutor(new NamedThreadFactory(poolName));
    }

    /**
     * 获取KitService中configSignature线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getConfSigTaskThreadExecutor() {
        if (configSignaturePool == null) {
            configSignaturePool = newCachedThreadPool(0, CONFIG_SIG_THREAD_MAX, THREAD_POOL_PREFIX_CONFIG_SIG);
        }
        return configSignaturePool;
    }

    /**
     * 获取KitService中versionTable线程池
     *
     * @return 线程池
     */
    public static synchronized ExecutorService getVersionTabTaskThreadExecutor() {
        if (versionTablePool == null) {
            versionTablePool = newCachedThreadPool(0, VERSION_TABLE_THREAD_MAX, THREAD_POOL_PREFIX_VERSION_TAB);
        }
        return versionTablePool;
    }
}
