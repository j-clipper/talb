package jclipper.talb.trace.spring.webflux;

import jclipper.talb.base.RpcType;
import jclipper.talb.base.TalbAttributesRequest;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.context.TalbContext;
import jclipper.talb.factory.TalbRequestAttributeTransferConfigProvider;
import jclipper.talb.factory.base.TalbObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 17:53.
 */
@Slf4j
public class TalbSpringWebfluxTraceHandler {


    private static volatile TalbSpringWebfluxTraceHandler handler;

    public static TalbSpringWebfluxTraceHandler loadInstance() {
        if (handler == null) {
            synchronized (TalbSpringWebfluxTraceHandler.class) {
                if (handler == null) {
                    handler = new TalbSpringWebfluxTraceHandler();
                }
            }
        }
        return handler;
    }

    public ServerWebExchange prevHandle(ServerWebExchange exchange) {

        RpcType rpcType = exchange.getAttribute(TalbConstants.RPC_TYPE_KEY);
        String appName = TalbContext.getAppName();

        /**
         * 从Request Header中获取上游的Trace信息
         */
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String traceId = headers.getFirst(TalbConstants.TRACE_KEY);
        String spanId = headers.getFirst(TalbConstants.SPAN_ID_KEY);

        String prevRpcType = headers.getFirst(TalbConstants.PREV_RPC_TYPE_KEY);
        String prevService = headers.getFirst(TalbConstants.PREV_SERVICE_KEY);
        String prevHost = headers.getFirst(TalbConstants.PREV_HOST);
        String prevIp = headers.getFirst(TalbConstants.PREV_IP_KEY);

        TalbRequest talbRequest = parseAttributeToRequest(exchange);
        TalbContext.newServerContext(traceId, spanId, prevService)
                .rpcType(prevRpcType)
                .host(prevHost)
                .ip(prevIp)
                .request(talbRequest)
        ;

        RpcType transferRpcType = rpcType != null ? rpcType : RpcType.SPRING_WEBFLUX;
        TalbContext.newLocalContext(appName, transferRpcType).request(talbRequest);

        exchange.getAttributes().put(TalbConstants.TALB_CONTEXT, TalbContext.getLocalContext());
        exchange.getAttributes().put(TalbConstants.TALB_REQUEST, talbRequest);
        exchange.getAttributes().put(TalbConstants.TALB_CONTEXT_ATTRS_FOR_NEXT, TalbContext.getTraceMapForNext());
        return exchange;
    }

    public ServerWebExchange postHandle(ServerWebExchange exchange) {
        //将本次调用的Trace信息放置到请求头中
        Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            //webflux是异步调用，threadLocal中取不到，改用在ServerWebExchange设值获取
            TalbContext context = exchange.getAttribute(TalbConstants.TALB_CONTEXT);
            Map<String, String> traceAttributes = exchange.getAttribute(TalbConstants.TALB_CONTEXT_ATTRS_FOR_NEXT);
            Map<String, String> requestAttributes = TalbContext.getRequestAttributesForNext(context);
            if (traceAttributes != null && traceAttributes.size() > 0) {
                traceAttributes.forEach(httpHeader::set);
            }
            if (requestAttributes != null && requestAttributes.size() > 0) {
                requestAttributes.forEach(httpHeader::set);
            }
        };
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
        return exchange.mutate().request(serverHttpRequest).build();

    }


    public void afterCompletion() {
        TalbContext.clearContext();
    }

    /**
     * 将上游请求的 TalbRequest.attribute 解析出来
     *
     * @param exchange ServerWebExchange
     * @return TalbAttributesRequest
     */
    private TalbRequest parseAttributeToRequest(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        TalbRequestAttributeTransferConfigProvider provider = TalbObjectFactory.getObject(TalbRequestAttributeTransferConfigProvider.class);
        if (provider == null || provider.whiteListKeys() == null || provider.whiteListKeys().size() == 0) {
            return null;
        }
        Set<String> keys = provider.whiteListKeys();
        Map<String, Object> attributes = new HashMap<>(keys.size());
        keys.forEach(key -> {
            String value = request.getHeaders().getFirst(key);
            if (value != null) {
                attributes.put(key, value);
            }
        });
        return new TalbAttributesRequest(attributes);
    }
}
