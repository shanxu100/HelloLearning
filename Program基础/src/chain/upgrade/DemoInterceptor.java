
import org.codestore.zerocode.designpattern.chain.Request;
import org.codestore.zerocode.designpattern.chain.Response;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class DemoInterceptor implements Interceptor {
    @Override
    public Response interceptor(Request request, Chain chain) throws ChainException {
        request.changeRequest("upgrade DemoInterceptor");
        Response response = null;
        response = chain.process(request);

        response.changeResponse("upgrade DemoInterceptor");
        return response;
    }
}
