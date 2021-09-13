

import com.google.gson.Gson;

/**
 * 功能描述
 *
 * @since 2020-12-16
 */
public class ReconnectionInfo {

    public int state = WsStatus.CODE.NETWORK_RECONNECTING;

    public int count;

    public long startConnectTime;

    public ReconnectionInfo(int count, long startConnectTime) {
        this.count = count;
        this.startConnectTime = startConnectTime;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
