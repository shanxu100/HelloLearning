
/**
 * websocket状态码
 *
 * @since 2020-05-17
 */

public class WsStatus {

    public final static int CONNECTED = 1;

    public final static int CONNECTING = 0;

    public final static int RECONNECT = 2;

    public final static int DISCONNECTED = -1;

    public static class CODE {

        public final static int NORMAL_CLOSE = 1000;

        public final static int ABNORMAL_CLOSE = 1001;

        /**
         * 鉴权失败
         */
        public final static int AUTHORIZATION_ERROR = 1002;

        /**
         * 网络连接失败
         */
        public final static int NETWORK_FAILURE = 1003;

        /**
         * websocket执行了重连，并且重连成功
         */
        public final static int NETWORK_RECONNECT_SUC = 1004;

        /**
         * 网络正在重连
         */
        public final static int NETWORK_RECONNECTING = 1005;

        /**
         * 网络第一次连接成功
         */
        public final static int NETWORK_CONNECT_SUC = 1006;

        /**
         * 网络第一次连接成功
         */
        public final static int INIT_WEB_SOCKET_FAILED = 1007;

        /**
         * 多终端登录被挤掉
         */
        public final static int SQUEEZE_OUT = 4000;

        /**
         * 注册失败
         */
        public final static int REGISTRATION_FAILURE = 4001;

        /**
         * 被管理员踢出房间
         */
        public final static int KICK_OUT = 4002;

        /**
         * 房间被管理员强制解散
         */
        public final static int ROOM_RELEASE = 4003;

        /**
         * 心跳超时
         */
        public final static int HEARTBEAT_TIMEOUT = 4004;

    }

    public static class TIP {

        public final static String NORMAL_CLOSE = "normal close";

        public final static String ABNORMAL_CLOSE = "abnormal close";
    }
}
