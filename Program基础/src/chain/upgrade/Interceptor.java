
import org.codestore.zerocode.designpattern.chain.Request;
import org.codestore.zerocode.designpattern.chain.Response;

/**
 * 功能描述
 *
 * @since 2020-12-25
 */
public interface Interceptor {


    Response interceptor(Request request, Chain chain) throws ChainException;


    interface Chain {


        void addInterceptor(Interceptor interceptor);


        Response process(Request request) throws ChainException;


    }

}
