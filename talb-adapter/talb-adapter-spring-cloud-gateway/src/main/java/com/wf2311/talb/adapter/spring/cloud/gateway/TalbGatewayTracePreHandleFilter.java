package com.wf2311.talb.adapter.spring.cloud.gateway;

import com.wf2311.talb.base.RpcType;
import com.wf2311.talb.base.TalbConstants;
import com.wf2311.talb.trace.spring.webflux.TalbSpringWebfluxTraceHandler;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Trace信息传递 GlobalFilter
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 19:22.
 */
public class TalbGatewayTracePreHandleFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        exchange.getAttributes().put(TalbConstants.RPC_TYPE_KEY, RpcType.SPRING_CLOUD_GATEWAY);

        return chain.filter(TalbSpringWebfluxTraceHandler.loadInstance().prevHandle(exchange))
                .doFinally(signalType -> TalbSpringWebfluxTraceHandler.loadInstance().afterCompletion());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
