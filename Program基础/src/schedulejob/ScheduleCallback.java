
/**
 * 功能描述
 *
 * @since 2020-07-10
 */
public interface ScheduleCallback {

    /**
     * 用户自定义的周期性执行任务
     */
    void onRepeat();

    /**
     * 停止执行用户自定义的周期性任务
     */
    void onStop();
}
