
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 功能描述
 *
 * @since 2020-06-17
 */
class NetworkUtil {

    /**
     * 获取手机IP
     * TODO 移动网络的IP + wifi网络的IP
     *
     * @return
     */
    public static List<String> getLocalNetworkIps() {
        String localIP = "127.0.0.1";
        List<String> hostList = new ArrayList<>();
        hostList.add(localIP);
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        hostList.add(inetAddress.getHostAddress());
                    }
                }

            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return hostList;
    }
}
