package jclipper.talb.adapter.spring.cloud.gateway;

import jclipper.talb.trace.spring.webflux.TalbSpringWebfluxTraceHandler;
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
public class TalbGatewayTracePostHandleFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(TalbSpringWebfluxTraceHandler.loadInstance().postHandle(exchange));
    }

    @Override
    public int getOrder() {
        return TalbReactiveLoadBalancerClientGlobalFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER + 1;
    }
}
