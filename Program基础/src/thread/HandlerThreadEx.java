

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
class HandlerThreadEx extends HandlerThread {

    private static final String TAG = "HandlerThreadEx";

    private boolean quit = false;

    public HandlerThreadEx(String name) {
        super(name);
    }

    public HandlerThreadEx(String name, int priority) {
        super(name, priority);
    }

    @Override
    public void run() {
        try {
            super.run();
        } catch (Exception e) {
            Log.e(TAG, "looper crashed");
            e.printStackTrace();
        }

        while (!quit) {
            try {
                Log.e(TAG, "re-loop");
                Looper.loop();
            } catch (Exception e) {
                Log.e(TAG, ".......");
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean quit() {
        quit = true;
        return super.quit();
    }

    @Override
    public boolean quitSafely() {
        quit = true;
        return super.quitSafely();
    }

    @Override
    public int getThreadId() {
        if (quit) {
            return -1;
        } else {
            return Process.myTid();
        }
    }
}
