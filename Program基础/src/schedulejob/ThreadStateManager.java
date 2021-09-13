
import android.util.Log;

/**
 * 负责管理任务调度的 start 和 stop 状态
 *
 * @since 2020-07-10
 */
class ThreadStateManager {

    /**
     * 0:停止
     * 1:运行
     */
    private int interrupted = 0;

    private static final String TAG = ThreadStateManager.class.getSimpleName();

    protected void start(long fixedDelay, ScheduleCallback callback) {
        if (!isInterrupted()) {
            Log.e(TAG, "current handler is running... need not to start twice");
            return;
        }
        Log.i(TAG, "change to START state.... ");
        interrupted = 1;
    }

    protected void stop() {
        Log.i(TAG, "change to STOP state.... ");
        interrupted = 0;
    }

    /**
     * @return true:已被终止 false：正在运行
     */
    public boolean isInterrupted() {
        return interrupted == 0;
    }
}
