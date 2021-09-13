
/**
 * 功能描述
 *
 * @since 2020-12-24
 */
public interface IWsMsgCallback {

    /**
     * 对应requestId的处理消息
     *
     * @param requestId 请求时的requestId
     * @param sentMsg 发送的原始消息内容
     * @param receivedMsg 接收的原始消息内容
     */
    void onMessage(String requestId, String type, String sentMsg, String receivedMsg);

    /**
     * 超时
     *
     * @param requestId 请求时的requestId
     * @param sentMsg 发送的原始消息内容
     */
    void onTimeout(String requestId, String sentMsg);
}
