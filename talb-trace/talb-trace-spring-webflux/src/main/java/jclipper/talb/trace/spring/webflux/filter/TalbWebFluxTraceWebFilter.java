package jclipper.talb.trace.spring.webflux.filter;

import jclipper.talb.base.RpcType;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.trace.spring.webflux.TalbSpringWebfluxTraceHandler;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 18:44.
 */
public class TalbWebFluxTraceWebFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        exchange.getAttributes().put(TalbConstants.RPC_TYPE_KEY, RpcType.SPRING_WEBFLUX);

        return chain.filter(TalbSpringWebfluxTraceHandler.loadInstance().prevHandle(exchange))
                .doFinally(signalType -> TalbSpringWebfluxTraceHandler.loadInstance().afterCompletion());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
