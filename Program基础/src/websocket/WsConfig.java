
/**
 * 功能描述
 *
 * @since 2020-08-06
 */
public class WsConfig {

    public String wsUrl;

    public String authorization;

    public String roomId;

    public String userId;

    public String appId;

    public String role;

    public String clientVersion;

    public String romVersion;

    public String devType;

    public String netType;

    public String traceId;

    public WsConfig() {
    }

    public WsConfig(String wsUrl, String authorization, String roomId, String userId, String appId, String role,
        String clientVersion, String romVersion, String devType, String netType, String traceId) {
        this.wsUrl = wsUrl;
        this.authorization = authorization;
        this.roomId = roomId;
        this.userId = userId;
        this.appId = appId;
        this.role = role;
        this.clientVersion = clientVersion;
        this.romVersion = romVersion;
        this.devType = devType;
        this.netType = netType;
        this.traceId = traceId;
    }

}
