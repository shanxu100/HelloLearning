
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * 公私钥生成
 *
 * @since 2020-05-13
 */
public final class KeyPairUtil {
    private static final String TAG = KeyPairUtil.class.getSimpleName();

    private static final ThreadLocal<FutureTask<KeyPair>> THREAD_LOCAL = new ThreadLocal<>();

    private static final String CONFERENCE_SECRET = "conference_secret";

    private static final String KEY_PAIR = "keyPair";

    private KeyPairUtil() {
    }

    /**
     * 单次公私钥对生成的异步同步任务
     *
     * @return KeyPair 可能为null
     */
    @Deprecated
    public static FutureTask<KeyPair> getKeyPairFutureTask() {
        FutureTask<KeyPair> futureTask = THREAD_LOCAL.get();
        if (null == futureTask) {
            futureTask = new FutureTask<>(KeyPairUtil::rsaEncrypt);
            THREAD_LOCAL.set(futureTask);
            AsyncExec.submitCalc(futureTask);
        }
        return futureTask;
    }

    /**
     * 单次公私钥对生成的异步同步任务
     * 如果存在缓存，则在缓存中读取已经生成的公私钥
     *
     * @return KeyPair 可能为null
     */
    public static FutureTask<KeyPair> getCachedKeyPairFutureTask(Context context) {
        FutureTask<KeyPair> futureTask = THREAD_LOCAL.get();
        if (null == futureTask) {
            futureTask = new FutureTask<>(() -> getCachedKeyPair(context));
            THREAD_LOCAL.set(futureTask);
            AsyncExec.submitCalc(futureTask);
        }
        return futureTask;
    }

    /**
     * 清理单次公私钥对生成的异步同步任务
     *
     * @return KeyPair 可能为null
     */
    public static void removeKeyPairFutureTask() {
        FutureTask<KeyPair> futureTask = THREAD_LOCAL.get();
        if (null != futureTask) {
            THREAD_LOCAL.remove();
        }
    }

    /**
     * 用SharedPreferences对加密后的KeyPair对象缓存，序列化反序列化。
     *
     * @param context 上下文
     * @return KeyPair
     */
    private static KeyPair getCachedKeyPair(Context context) {
        final SharedPreferences sharedPreferences =
            context.getSharedPreferences(CONFERENCE_SECRET, Context.MODE_PRIVATE);
        String keyPairStr = sharedPreferences.getString(KEY_PAIR, null);
        if (null == keyPairStr || keyPairStr.isEmpty()) {
            KeyPair keyPair = rsaEncrypt();
            if (null == keyPair) {
                Log.e(TAG, "gen keyPair error");
                return null;
            }
            AsyncExec.submitCalc(() -> {
                String strKeyPair = Arrays.toString(SerializeUtil.object2Bytes(keyPair));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_PAIR, EncryptUtil.aesEncrypt(strKeyPair));
                editor.apply();
            });
            return keyPair;
        }
        Object object = SerializeUtil.byte2Object(EncryptUtil.aesDecrypt(keyPairStr).getBytes(StandardCharsets.UTF_8));
        if (!(object instanceof KeyPair)) {
            Log.e(TAG, "keyPair deSerialize failed");
            return null;
        }
        return (KeyPair) object;
    }

    /**
     * 生成公私钥对，RSAEncrypt方式！！！
     *
     * @return KeyPair 可能为null
     */
    private static KeyPair rsaEncrypt() {
        final String publicKeyTag = "publicKey";
        final String privateKeyTag = "privateKey";
        try {
            Map<String, Key> map = RSAEncrypt.generateRSAKeyPair(4096);
            if (!map.containsKey(publicKeyTag) || !map.containsKey(privateKeyTag)) {
                return null;
            }
            final Key pubKey = map.get(publicKeyTag);
            final Key priKey = map.get(privateKeyTag);
            if (null == pubKey || null == priKey) {
                return null;
            }
            if (!(pubKey instanceof PublicKey && priKey instanceof PrivateKey)) {
                Log.e(TAG, "keypair type wrong");
                return null;
            }
            return new KeyPair((PublicKey) pubKey, (PrivateKey) priKey);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 生成公私钥
     *
     * @return KeyPair 可能为null
     */
    @Deprecated
    public static KeyPair generateKeyPair() {
        return generateKeyPair("RSA", 4096);
    }

    /**
     * 生成公私钥
     *
     * @param algorithm 算法
     * @param keysize 长度
     * @return KeyPair 可能为null
     */
    @Deprecated
    public static KeyPair generateKeyPair(String algorithm, int keysize) {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        keyPairGenerator.initialize(keysize, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }
}
