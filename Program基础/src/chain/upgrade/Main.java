

import org.codestore.zerocode.designpattern.chain.Request;
import org.codestore.zerocode.designpattern.chain.Response;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class Main {

    public static void main(String[] args) throws ChainException {

        Request request = new Request();

        Interceptor.Chain chain = new RealChain();
        chain.addInterceptor(new DemoInterceptor());
        chain.addInterceptor(new FirstInterceptor());
        chain.addInterceptor(new FinalInterceptor());

        // 只能处理一个request。当处理第二个request的时候，需要reset chain
        Response response = chain.process(request);

        System.out.println(response.toString());


    }


    private static final class FirstInterceptor implements Interceptor {

        @Override
        public Response interceptor(Request request, Chain chain) throws ChainException {
            request.changeRequest("upgrade FirstInterceptor");
            Response response = null;
            response = chain.process(request);

            response.changeResponse("upgrade FirstInterceptor");
            return response;
        }
    }


    private static final class FinalInterceptor implements Interceptor {

        @Override
        public Response interceptor(Request request, Chain chain) throws ChainException {
            request.changeRequest("upgrade FinalInterceptor");
            System.out.println(request.toString());

            Response response = null;
            try {
                response = chain.process(request);
            } catch (ChainException e) {
//                e.printStackTrace();
                response = new Response();
                System.out.println("no more interceptor....create a new Response");
            }

            response.changeResponse("upgrade FinalInterceptor");
            return response;
        }
    }


}
