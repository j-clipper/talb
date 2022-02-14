package jclipper.talb.trace.spring.web;

import jclipper.talb.base.RpcType;
import jclipper.talb.base.TalbAttributesRequest;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.context.TalbContext;
import jclipper.talb.factory.TalbRequestAttributeTransferConfigProvider;
import jclipper.talb.factory.base.TalbObjectFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 18:48.
 */
public class TalbSpringWebHandler {

    private static volatile TalbSpringWebHandler handler;

    public static TalbSpringWebHandler loadInstance() {
        if (handler == null) {
            synchronized (TalbSpringWebHandler.class) {
                if (handler == null) {
                    handler = new TalbSpringWebHandler();
                }
            }
        }
        return handler;
    }

    public void prevHandle(HttpServletRequest request) {
        String traceId = request.getHeader(TalbConstants.TRACE_KEY);
        String spanId = request.getHeader(TalbConstants.SPAN_ID_KEY);

        String prevRpcType = request.getHeader(TalbConstants.PREV_RPC_TYPE_KEY);
        String prevService = request.getHeader(TalbConstants.PREV_SERVICE_KEY);
        String prevHost = request.getHeader(TalbConstants.PREV_HOST);
        String prevIp = request.getHeader(TalbConstants.PREV_IP_KEY);
        TalbRequest talbRequest = parseAttributeToRequest(request);

        TalbContext.newServerContext(traceId, spanId, prevService)
                .rpcType(prevRpcType)
                .host(prevHost)
                .ip(prevIp)
                .request(talbRequest)
        ;

        String appName = TalbContext.getAppName();
        TalbContext.newLocalContext(appName, RpcType.SPRING_MVC).request(talbRequest);
    }

    /**
     * 将上游请求的 TalbRequest.attribute 解析出来
     *
     * @param request HttpServletRequest
     * @return TalbAttributesRequest
     */
    private TalbRequest parseAttributeToRequest(HttpServletRequest request) {
        TalbRequestAttributeTransferConfigProvider provider = TalbObjectFactory.getObject(TalbRequestAttributeTransferConfigProvider.class);
        if (provider == null || provider.whiteListKeys() == null || provider.whiteListKeys().size() == 0) {
            return null;
        }
        Set<String> keys = provider.whiteListKeys();
        Map<String, Object> attributes = new HashMap<>(keys.size());
        keys.forEach(key -> {
            String value = request.getHeader(key);
            if (value != null) {
                attributes.put(key, value);
            }
        });
        return new TalbAttributesRequest(attributes);
    }

    /**
     * TODO
     *
     * @param request
     * @param response
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> traceAttributes = TalbContext.getTraceMapForNext();
        Map<String, String> requestAttributes = TalbContext.getRequestAttributesForNext();
        if (traceAttributes.size() > 0) {
            traceAttributes.forEach(response::setHeader);
        }
        if (requestAttributes != null && requestAttributes.size() > 0) {
            requestAttributes.forEach(response::setHeader);
        }
    }

    public void afterCompletion() {
        TalbContext.clearContext();
    }
}
