

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

/**
 * 对HandlerThread进行扩展：
 * 当looper中的message发生了未捕获的crash，可以在该类中捕获，并不中断looper
 *
 * @since 2021-01-09
 */
public class HandlerThreadEx extends HandlerThread {

    private static final String TAG = "HandlerThreadEx";

    private volatile boolean quit = false;

    public HandlerThreadEx(String name) {
        super(name);
    }

    public HandlerThreadEx(String name, int priority) {
        super(name, priority);
    }

    @Override
    public void run() {
        super.run();
        while (!quit) {
            // loop()开启死循环执行Handler中queue中的任务。
            // 当其中的某个任务发生crash后，会中断loop中的循环
            // 此处，增加一层while死循环：在Looper发生crash时，重新调用loop()开启新一轮循环
            try {
                Looper.loop();
            } catch (Exception e) {
                Log.e(TAG, "HandlerThreadEx: Internal Looper crashed -- Looper.loop()", e);
            }
        }
    }

    @Override
    public boolean quit() {
        Log.i(TAG, "HandlerThreadEx quit");
        return quit = super.quit();
    }

    @Override
    public boolean quitSafely() {
        Log.i(TAG, "HandlerThreadEx quitSafely");
        return quit = super.quitSafely();
    }

    @Override
    public int getThreadId() {
        // 此处不能使用 super.getThreadId() !!!!!!
        // 从HandlerThread源码中看，在run()函数中，当Looper.loop()因为某种原因退出死循环后，
        // 表示线程号的mTid值将被赋值为-1
        // 在本类中的run()函数中，通过Looper.loop()再起拉起Handler循环后，mTid无法再赋新值
        // 因此，此处重新实现 getThreadId() 方法
        Log.i(TAG, "getThreadId");
        return quit ? -1 : Process.myTid();
    }
}
