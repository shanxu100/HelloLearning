
import org.codestore.zerocode.designpattern.chain.Request;
import org.codestore.zerocode.designpattern.chain.Response;

import java.util.LinkedList;
import java.util.List;

/**
 * 功能描述
 *
 * @since 2020-12-26
 */
public class RealChain implements Interceptor.Chain {
    private List<Interceptor> interceptors = new LinkedList<>();
    private int index = 0;

    public RealChain() {
        this.index = 0;
    }

    @Override
    public void addInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            interceptors.add(interceptor);
        }
    }

    @Override
    public Response process(Request request) throws ChainException {
        Interceptor interceptor = null;
        if (index >= interceptors.size()) {
            throw new ChainException();
        }
        interceptor = interceptors.get(index);
        index++;

        return interceptor.interceptor(request, this);
    }


}
