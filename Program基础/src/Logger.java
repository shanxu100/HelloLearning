
import java.util.IllegalFormatException;

import android.util.Log;

/**
 * @version [V2.1.4.100, 2018/12/14]
 * @hide 统一管理log打印
 * @since V2.1.4.100
 */
public class Logger {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TAG = "NetworkKit_Logger";

    // 统一的tag前缀
    private static final String PREFIX_TAG = "DF_";

    /**
     * 打印测试log release版本此部分代码会被编译去除，debug版本用于测试
     *
     * @param tag     日志tag
     * @param format  StringFormat
     * @param objects 打印的数据
     */
    public static void v(String tag, String format, Object... objects) {
        if (DEBUG) {
            if (format == null) {
                Log.w(TAG, "format is null, not log");
                return;
            }
            try {
                String log = StringUtils.format(format, objects);
                Log.v(complexTag(tag), log);
            } catch (IllegalFormatException e) {
                Logger.w(TAG, "log format error" + format, e);
            }
        }
    }

    /**
     * 打印测试log release版本此部分代码会被编译去除，debug版本用于测试
     *
     * @param tag    日志tag
     * @param object 打印的数据
     */
    public static void v(String tag, Object object) {
        if (DEBUG) {
            Log.v(complexTag(tag), object == null ? "null" : object.toString());
        }
    }

    /**
     * 打印测试log release版本此部分代码会被编译去除，debug版本用于测试
     *
     * @param tag    日志tag
     * @param object 打印的数据
     */
    public static void d(String tag, Object object) {
        if (DEBUG) {
            Log.d(complexTag(tag), object == null ? "null" : object.toString());
        }
    }

    /**
     * 打印测试log release版本此部分代码会被编译去除，debug版本用于测试
     *
     * @param tag     日志tag
     * @param format  StringFormat
     * @param objects 打印的数据
     */
    public static void d(String tag, String format, Object... objects) {
        if (DEBUG) {
            if (format == null) {
                Log.w(TAG, "format is null, not log");
                return;
            }
            try {
                String log = StringUtils.format(format, objects);
                Log.d(complexTag(tag), log);
            } catch (IllegalFormatException e) {
                Logger.w(TAG, "log format error" + format, e);
            }
        }
    }

    /**
     * 打印测试log
     *
     * @param tag    日志tag
     * @param object 打印的数据
     */
    public static void i(String tag, Object object) {
        Log.i(complexTag(tag), object == null ? "null" : object.toString());
    }

    /**
     * 打印测试log 试
     *
     * @param tag     日志tag
     * @param format  StringFormat
     * @param objects 打印的数据
     */
    public static void i(String tag, String format, Object... objects) {
        if (format == null) {
            Log.w(TAG, "format is null, not log");
            return;
        }
        try {
            String log = StringUtils.format(format, objects);
            Log.i(complexTag(tag), log);
        } catch (IllegalFormatException e) {
            Logger.w(TAG, "log format error" + format, e);
        }
    }

    /**
     * 打印测试log
     *
     * @param tag    日志tag
     * @param object 打印的数据
     */
    public static void w(String tag, Object object) {
        Log.w(complexTag(tag), object == null ? "null" : object.toString());
    }

    /**
     * 打印测试log
     *
     * @param tag     日志tag
     * @param format  StringFormat
     * @param objects 打印的数据
     */
    public static void w(String tag, String format, Object... objects) {
        if (format == null) {
            Log.w(TAG, "format is null, not log");
            return;
        }
        try {
            String log = StringUtils.format(format, objects);
            Log.w(complexTag(tag), log);
        } catch (IllegalFormatException e) {
            Logger.w(TAG, "log format error" + format, e);
        }
    }

    /**
     * 打印测试log
     *
     * @param tag 日志tag
     * @param s   自定义描述信息
     * @param e   异常内容和堆栈
     */
    public static void w(String tag, String s, Throwable e) {
        Log.w(complexTag(tag), s, getNewThrowable(e));
    }

    /**
     * 打印测试log
     *
     * @param tag    日志tag
     * @param object 打印的数据
     */
    public static void e(String tag, Object object) {
        Log.e(complexTag(tag), object == null ? "null" : object.toString());
    }

    /**
     * 打印测试log release版本此部分代码会被编译去除，debug版本用于测试
     *
     * @param tag     日志tag
     * @param format  StringFormat
     * @param objects 打印的数据
     */
    public static void e(String tag, String format, Object... objects) {
        if (format == null) {
            Log.w(TAG, "format is null, not log");
            return;
        }
        try {
            String log = StringUtils.format(format, objects);
            Log.e(complexTag(tag), log);
        } catch (IllegalFormatException e) {
            Logger.w(TAG, "log format error" + format, e);
        }
    }

    /**
     * 打印测试log
     *
     * @param tag 日志tag
     * @param s   自定义描述信息
     * @param e   异常内容和堆栈
     */
    public static void e(String tag, String s, Throwable e) {
        Log.e(complexTag(tag), s, getNewThrowable(e));
    }

    /**
     * 匿名化异常的message信息，debug版本不做匿名化，方便查看log
     *
     * @param e 需要匿名化信息的异常
     * @return 匿名化信息后的异常
     */
    private static Throwable getNewThrowable(Throwable e) {
        if (DEBUG) {
            return e;
        }
        if (e == null) {
            return null;
        } else {
            ThrowableWrapper retWrapper = new ThrowableWrapper(e);
            retWrapper.setStackTrace(e.getStackTrace());
            retWrapper.setMessage(StringUtils.anonymizeMessage(e.getMessage()));
            ThrowableWrapper preWrapper = retWrapper;
            // 递归修改cause的message消息
            for (Throwable currThrowable = e.getCause();
                    currThrowable != null; currThrowable = currThrowable.getCause()) {
                ThrowableWrapper currWrapper = new ThrowableWrapper(currThrowable);
                currWrapper.setStackTrace(currThrowable.getStackTrace());
                currWrapper.setMessage(StringUtils.anonymizeMessage(currThrowable.getMessage()));
                preWrapper.setCause(currWrapper);
                preWrapper = currWrapper;
            }
            return retWrapper;
        }
    }

    /**
     * 打log增加统一tag标识 {@link #PREFIX_TAG}
     *
     * @param tag tag
     * @return 新tag
     */
    private static String complexTag(String tag) {
        return PREFIX_TAG + tag;
    }

    /**
     * 异常包装对象 <功能详细描述>
     *
     * @version [版本号, 2017年7月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    private static class ThrowableWrapper extends Throwable {
        /**
         * 序列化id
         */
        private static final long serialVersionUID = 7129050843360571879L;

        /**
         * 异常消息内容(修改后)
         */
        private String message;

        /**
         * 异常原因
         */
        private Throwable thisCause;

        /**
         * 包装的Throwable对象
         */
        private Throwable ownerThrowable;

        private ThrowableWrapper(Throwable t) {
            ownerThrowable = t;
        }

        @Override
        public Throwable getCause() {
            return thisCause == this ? null : thisCause;
        }

        private void setCause(Throwable cause) {
            thisCause = cause;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            if (ownerThrowable == null) {
                return "";
            }

            String throwableClzName = ownerThrowable.getClass().getName();
            if (message != null) {
                String prefix = throwableClzName + ": ";
                if (message.startsWith(prefix)) {
                    return message;
                } else {
                    return prefix + message;
                }
            } else {
                return throwableClzName;
            }
        }
    }
}
