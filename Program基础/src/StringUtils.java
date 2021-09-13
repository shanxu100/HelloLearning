
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import android.text.TextUtils;

/**
 * 字符串操作Utils
 *
 * @version [V2.1.4.100, 2018/12/14]
 * @since V2.1.4.100
 */
public class StringUtils {
    private static final String TAG = "StringUtils";

    /**
     * 判断字符串是否相同
     *
     * @param first  第一个字符串
     * @param second 第二个字符串
     * @return 如果是字符串相同，则返回true
     */
    public static boolean strEquals(String first, String second) {
        return (first == second) || (first != null && first.equals(second));
    }

    /**
     * 将byte转为String,使用UTF-8， 如果出现异常或者输入为null,返回""
     *
     * @param bytes 输入的字节数组
     * @return 转化后的String
     */
    public static String byte2Str(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.w("StringUtils.byte2str error: UnsupportedEncodingException", e);
        }
        return "";
    }

    /**
     * 转化String为字节数组，如果输入为空或者异常，返回0长度数组
     *
     * @param text 输入字符串
     * @return 输出转化后的字节数组
     */
    public static byte[] str2Byte(String text) {
        if (TextUtils.isEmpty(text)) {
            return new byte[0];
        }
        try {
            return text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.w("StringUtils.str2Byte error: UnsupportedEncodingException", e);
        }
        return new byte[0];
    }

    /**
     * 匿名化处理
     *
     * @param message 需要做匿名化的数据
     * @return 匿名处理后的结果
     */
    public static String anonymizeMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return message;
        } else {
            char[] messageChars = message.toCharArray();
            for (int i = 0; i < messageChars.length; i++) {
                if (i % 2 == 1) {
                    messageChars[i] = '*';
                }
            }
            return new String(messageChars);
        }
    }

    /**
     * 转化long数据为字节数组
     *
     * @param content 输入
     * @return 转换后的数据
     */
    public static byte[] getBytes(long content) {
        return getBytes(String.valueOf(content));
    }

    /**
     * 转化String为字节数组，如果输入为空或者异常，返回0长度数组
     *
     * @param content 输入字符串
     * @return 输出转化后的字节数组
     */
    public static byte[] getBytes(String content) {
        byte[] bytes = new byte[0];
        if (content == null) {
            return bytes;
        }
        try {
            bytes = content.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            Logger.w(TAG, "the content has error while it is converted to bytes");
        }
        return bytes;
    }

    /**
     * 用指定的语言环境，格式字符串返回格式化的字符串,默认值{@link Locale#ENGLISH}
     *
     * @param format  A <a href="../util/Formatter.html#syntax">format string</a>
     * @param objects 格式中格式说明符引用的参数串。如果参数多于格式说明符，则忽略额外参数。 参数的数量是变量，可能为零。 参数的最大数量是 最大长度为受限定的Java数组的最大维度。
     * @return 格式化后字符串
     * @throws java.util.IllegalFormatException format包含非法语法，或者objects给定参数不足
     * @see java.util.Formatter
     * @see String#format(String, Object...)
     */
    public static String format(String format, Object... objects) {
        if (format == null) {
            return "";
        }
        return String.format(Locale.ROOT, format, objects);
    }

    /**
     * 转化字符串为小写
     *
     * @param source 需被转化的字符串
     * @return 转化后的小写字符串
     */
    public static String toLowerCase(String source) {
        if (source == null) {
            return source;
        }
        return source.toLowerCase(Locale.ROOT);
    }

    /**
     * 在字符串 content 中，寻找倒数第N个str后面的字符串
     *
     * @return
     */
    public static String getLastN(String content, String str, int N) {
        String[] cs = content.split(str);
        if (cs.length < N) {
            return content;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = cs.length - N; i < cs.length; i++) {
            sb.append(cs[i]).append(str);
        }
        String res = sb.toString();
        if (res.length() != 0) {
            res = res.substring(0, res.length() - str.length());
        }
        return res;
    }
}

