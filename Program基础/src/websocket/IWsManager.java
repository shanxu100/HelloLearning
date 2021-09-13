
/**
 * WebSocket状态/事件等管理接口
 *
 * @since 2020-05-29
 */
public interface IWsManager {

    /**
     * WebSocket建立连接
     *
     * @param wsConfig wsConfig
     * @param wsCallback wsCallbacko
     */
    void startConnection(WsConfig wsConfig, IWsCallback wsCallback);

    /**
     * WebSocket关闭连接
     */
    void stopConnection();

    /**
     * 刷新鉴权
     *
     * @param authorization authorization
     */
    void renewToken(String authorization);

    /**
     * 发送消息的具体实现
     *
     * @param requestId requestId
     * @param type type
     * @param msg msg
     * @param checkConnection true:如果websocket已经未连接，则直接返回 ； 反之，则为false
     * @param callback callback
     * @return 是否发送成功
     */
    boolean sendMessage(String requestId, String type, String msg, boolean checkConnection, IWsMsgCallback callback);

}
