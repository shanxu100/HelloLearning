
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * 定时任务：使用Handler实现周期性执行某项任务
 * 增加try-catch逻辑，即使在执行开发者的任务时发生了crash，依然不会中断该周期性的执行计划
 *
 * @since 2020-07-10
 */
public class ScheduleJobHandler extends ThreadStateManager {

    private Handler handler = null;

    private ScheduleCallback callback = null;

    private static final String TAG = ScheduleJobHandler.class.getSimpleName();

    private static final class InstanceHolder {
        private static ScheduleJobHandler INSTANCE = new ScheduleJobHandler(Looper.getMainLooper());
    }

    /**
     * 获取一个默认实例：在主线程执行定时任务
     *
     * @return
     */
    public static ScheduleJobHandler getDefault() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 创建一个新实例：由上层应用指定线程并管理该实例
     *
     * @return
     */
    public static ScheduleJobHandler newInstance(Looper looper) {
        return new ScheduleJobHandler(looper);
    }

    private ScheduleJobHandler(Looper looper) {
        handler = new Handler(looper);
    }

    /**
     * 开启周期性任务
     *
     * @param fixedDelay 定时任务的执行周期，即上一次任务结束之后，等待 fixedDelay 毫秒再开始执行本次任务
     * @param callback   定时任务的回调。开发者实现该接口后做自己的定时任务
     */
    @Override
    public void start(long fixedDelay, ScheduleCallback callback) {
        super.start(fixedDelay, callback);
        handler.removeCallbacksAndMessages(null);
        this.callback = callback;
        trigger(fixedDelay, callback);
    }

    /**
     * 结束周期性任务
     */
    @Override
    public void stop() {
        super.stop();
        Log.i(TAG, "stop schedule jobs.... ");
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 实现周期性执行任务的逻辑
     * 增加try-catch逻辑，即使在执行开发者的任务时发生了crash，依然不会中断该周期性的执行计划
     */
    private void trigger(long fixedDelay, ScheduleCallback callback) {
        if (isInterrupted()) {
            Log.e(TAG, "current handler has been interrupted");
            if (callback != null) {
                callback.onStop();
            }
            return;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "execute the job once....start");
                try {
                    callback.onRepeat();
                } catch (Exception e) {
                    Log.e(TAG, "schedule job ... uncatched exception");
                } catch (Error e) {
                    Log.e(TAG, "schedule job ... uncatched error");
                }
                // 采用递归的方式，触发下一次的任务，以此实现“周期性执行”
                trigger(fixedDelay, callback);
                Log.i(TAG, "execute the job once....finish");
            }
        }, fixedDelay);
    }

}
