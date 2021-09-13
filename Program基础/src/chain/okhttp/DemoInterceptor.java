
import org.codestore.zerocode.designpattern.chain.Request;
import org.codestore.zerocode.designpattern.chain.Response;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class DemoInterceptor implements Interceptor {
    @Override
    public Response interceptor(Request request, Chain chain) {
        request.changeRequest("DemoInterceptor");
        Response response = chain.process(request);
        response.changeResponse("DemoInterceptor");
        return response;

    }
}
