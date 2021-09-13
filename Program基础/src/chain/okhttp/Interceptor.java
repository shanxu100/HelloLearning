
import org.codestore.zerocode.designpattern.chain.Request;
import org.codestore.zerocode.designpattern.chain.Response;

/**
 * 功能描述
 *
 * @since 2020-12-25
 */
public interface Interceptor {

    /**
     * 做具体的 interceptor 处理逻辑
     * 如果需要下一个interceptor继续处理，则调用 chain.process 获取response
     * 否则，自己创建一个response返回
     *
     * @param request request 上一个 interceptor 处理后的 request，即本interceptor可以处理的request
     * @param chain   chain
     * @return response
     */
    Response interceptor(Request request, Chain chain);


    interface Chain {

        /**
         * 将当前的 interceptor 处理的 request，交给下一个interceptor处理，并获取response
         *
         * @param request 当前 interceptor 需要处理的 request
         * @return 处理的结果
         */
        Response process(Request request);

    }

}
