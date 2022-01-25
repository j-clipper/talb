package com.wf2311.talb.adapter.spring.cloud.gateway.configuration;

import com.wf2311.talb.adapter.spring.cloud.configuration.TalbSpringCloudCommonConfiguration;
import com.wf2311.talb.adapter.spring.cloud.gateway.TalbGatewayTracePostHandleFilter;
import com.wf2311.talb.adapter.spring.cloud.gateway.TalbGatewayTracePreHandleFilter;
import com.wf2311.talb.adapter.spring.cloud.gateway.TalbReactiveLoadBalancerClientGlobalFilter;
import com.wf2311.talb.base.LoadBalancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 15:55.
 */
@Configuration
@Import({TalbSpringCloudCommonConfiguration.class})
public class TalbSpringCloudGatewayConfiguration {

    @Bean
    @ConditionalOnMissingBean({TalbReactiveLoadBalancerClientGlobalFilter.class})
    @ConditionalOnBean(LoadBalancer.class)
    public TalbReactiveLoadBalancerClientGlobalFilter talbReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory
            , GatewayLoadBalancerProperties properties, LoadBalancerProperties loadBalancerProperties
            , LoadBalancer loadBalancer) {
        return new TalbReactiveLoadBalancerClientGlobalFilter(clientFactory, properties, loadBalancerProperties, loadBalancer);
    }

    @Bean
    @ConditionalOnMissingBean({TalbGatewayTracePreHandleFilter.class})
    public TalbGatewayTracePreHandleFilter talbGatewayTracePreHandleFilter() {
        return new TalbGatewayTracePreHandleFilter();
    }


    @Bean
    @ConditionalOnMissingBean({TalbGatewayTracePostHandleFilter.class})
    public TalbGatewayTracePostHandleFilter talbGatewayTracePostHandleFilter() {
        return new TalbGatewayTracePostHandleFilter();
    }
}
