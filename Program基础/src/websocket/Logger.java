
import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 功能描述
 *
 * @since 2020-12-24
 */
public class Logger {
    /**
     * 打印测试log release版本此部分代码会被编译去除，debug版本用于测试
     *
     * @param tag 日志tag
     * @param object 打印的数据
     */
    public static void d(String tag, Object object) {
        Log.d(tag, object.toString());
    }

    /**
     * 打印测试log release版本此部分代码会被编译去除，debug版本用于测试
     *
     * @param tag 日志tag
     * @param format StringFormat
     * @param objects 打印的数据
     */
    public static void d(String tag, String format, Object... objects) {
        Log.d(tag, format);

    }

    /**
     * 打印测试log
     *
     * @param tag 日志tag
     * @param object 打印的数据
     */
    public static void i(String tag, Object object) {
        Log.i(tag, object.toString());
    }

    /**
     * 打印测试log 试
     *
     * @param tag 日志tag
     * @param format StringFormat
     * @param objects 打印的数据
     */
    @SuppressLint("LogTagMismatch")
    public static void i(String tag, String format, Object... objects) {
        Log.i(tag, format);
    }

    /**
     * 打印测试log
     *
     * @param tag 日志tag
     * @param object 打印的数据
     */
    public static void w(String tag, Object object) {
        Log.w(tag, object.toString());
    }

    /**
     * 打印测试log
     *
     * @param tag 日志tag
     * @param msg 自定义描述信息
     * @param e 异常内容和堆栈
     */
    public static void w(String tag, String msg, Throwable e) {
        Log.w(tag, msg, e);
    }

    /**
     * 打印测试log
     *
     * @param tag 日志tag
     * @param msg 自定义描述信息
     */
    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    /**
     * 打印测试log
     *
     * @param tag 日志tag
     * @param msg 自定义描述信息
     * @param e 异常内容和堆栈
     */
    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
    }

}
