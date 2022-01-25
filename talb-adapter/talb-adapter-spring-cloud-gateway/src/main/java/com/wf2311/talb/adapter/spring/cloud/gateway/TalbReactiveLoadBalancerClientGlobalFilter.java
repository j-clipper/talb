package com.wf2311.talb.adapter.spring.cloud.gateway;

import com.wf2311.talb.adapter.spring.cloud.adapter.SpringCloudRequestDataRequest;
import com.wf2311.talb.adapter.spring.cloud.loadbalancer.TalbReactorLoadBalancer;
import com.wf2311.talb.base.LoadBalancer;
import com.wf2311.talb.base.TalbConstants;
import com.wf2311.talb.base.TalbRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 17:19.
 */
@Slf4j
public class TalbReactiveLoadBalancerClientGlobalFilter implements GlobalFilter, Ordered {

    /**
     * Order of filter.
     */
    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;

    private final LoadBalancerClientFactory clientFactory;

    private final GatewayLoadBalancerProperties properties;

    private final LoadBalancerProperties loadBalancerProperties;

    private final LoadBalancer loadBalancer;

    public TalbReactiveLoadBalancerClientGlobalFilter(LoadBalancerClientFactory clientFactory,
                                                      GatewayLoadBalancerProperties properties, LoadBalancerProperties loadBalancerProperties,
                                                      LoadBalancer loadBalancer) {
        this.clientFactory = clientFactory;
        this.properties = properties;
        this.loadBalancerProperties = loadBalancerProperties;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER - 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        // preserve the original url
        ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);

        if (log.isTraceEnabled()) {
            log.trace(TalbReactiveLoadBalancerClientGlobalFilter.class.getSimpleName() + " url before: " + url);
        }

        URI requestUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String serviceId = requestUri.getHost();
        Set<LoadBalancerLifecycle> supportedLifecycleProcessors = LoadBalancerLifecycleValidator
                .getSupportedLifecycleProcessors(clientFactory.getInstances(serviceId, LoadBalancerLifecycle.class),
                        RequestDataContext.class, ResponseData.class, ServiceInstance.class);
        DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest<>(new RequestDataContext(
                new RequestData(exchange.getRequest()), getHint(serviceId, loadBalancerProperties.getHint())));
        return choose(lbRequest, serviceId, supportedLifecycleProcessors, exchange).doOnNext(response -> {

                    if (!response.hasServer()) {
                        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                                .onComplete(new CompletionContext<>(CompletionContext.Status.DISCARD, lbRequest, response)));
                        throw NotFoundException.create(properties.isUse404(), "Unable to find instance for " + url.getHost());
                    }

                    ServiceInstance retrievedInstance = response.getServer();

                    URI uri = exchange.getRequest().getURI();

                    // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
                    // if the loadbalancer doesn't provide one.
                    String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";
                    if (schemePrefix != null) {
                        overrideScheme = url.getScheme();
                    }

                    DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(retrievedInstance,
                            overrideScheme);

                    URI requestUrl = reconstructURI(serviceInstance, uri);

                    if (log.isTraceEnabled()) {
                        log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                    }
                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR, response);
                    supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStartRequest(lbRequest, response));
                }).then(chain.filter(exchange))
                .doOnError(throwable -> supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                        .onComplete(new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(
                                CompletionContext.Status.FAILED, throwable, lbRequest,
                                exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR)))))
                .doOnSuccess(aVoid -> supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                        .onComplete(new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(
                                CompletionContext.Status.SUCCESS, lbRequest,
                                exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR),
                                new ResponseData(exchange.getResponse(), new RequestData(exchange.getRequest()))))));
    }

    protected URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    private Mono<Response<ServiceInstance>> choose(Request<RequestDataContext> lbRequest, String serviceId,
                                                   Set<LoadBalancerLifecycle> supportedLifecycleProcessors, ServerWebExchange exchange) {
        Object context = lbRequest.getContext();
        RequestData requestData = ((RequestDataContext) context).getClientRequest();
        TalbRequest talbRequest = SpringCloudRequestDataRequest.adapter(serviceId, requestData);

        TalbRequest existTalbRequest = exchange.getAttribute(TalbConstants.TALB_REQUEST);
        if (existTalbRequest != null && existTalbRequest.getAttributes() != null) {
            talbRequest.getAttributes().putAll(existTalbRequest.getAttributes());
        }

//        TalbReactorLoadBalancer loadBalancer = new TalbReactorLoadBalancer(clientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId, this.loadBalancer);
        ReactorLoadBalancer<ServiceInstance> loadBalancer = this.clientFactory.getInstance(serviceId,
                ReactorServiceInstanceLoadBalancer.class);
        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(lbRequest));
        if(!(loadBalancer instanceof TalbReactorLoadBalancer)){
            return loadBalancer.choose(lbRequest);
        }
        Mono<Response<ServiceInstance>> choose = ((TalbReactorLoadBalancer)loadBalancer).choose(lbRequest,talbRequest);
        //PUT talbRequest
        exchange.getAttributes().put(TalbConstants.TALB_REQUEST, talbRequest);

        return choose;
    }

    private String getHint(String serviceId, Map<String, String> hints) {
        String defaultHint = hints.getOrDefault("default", "default");
        String hintPropertyValue = hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }

}