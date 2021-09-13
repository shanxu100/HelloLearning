

/**
 * WebSocket连接一些状态h回调
 *
 * @since 2020-05-29
 */
public interface IWsCallback {

    /**
     * 收到服务器返回的数据
     *
     * @param text 消息文本
     */
    void onPush(String text);

    /**
     * WebSocket正在关闭
     *
     * @param code code码
     * @param reason 原因
     */
    void onCode(int code, String reason);

}
