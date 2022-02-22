package jclipper.talb.adapter.spring.boot.configuration;

import jclipper.talb.adapter.spring.boot.factory.DefaultGlobalPreferredNetworkConfigProvider;
import jclipper.talb.adapter.spring.boot.factory.DefaultRequestPreferredIpConfigProvider;
import jclipper.talb.adapter.spring.boot.factory.DefaultRequestPreferredNetworkConfigProvider;
import jclipper.talb.adapter.spring.boot.factory.DefaultRequestPreferredVersionConfigProvider;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.LoadBalancer;
import jclipper.talb.factory.*;
import jclipper.talb.factory.base.TalbObjectFactory;
import jclipper.talb.factory.internal.DefaultConsistentHashLoadBalancerConfigProvider;
import jclipper.talb.loadbalance.filter.*;
import jclipper.talb.loadbalance.ConsistentHashLoadBalancer;
import jclipper.talb.loadbalance.FilterInstanceLoadBalancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/19 17:35.
 */
@Configuration
public class TalbLoadBalanceDefaultConfiguration {
    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(RequestPreferredIpConfigProvider.class)
    public RequestPreferredIpConfigProvider requestPreferredIpConfigProvider(TalbLoadBalanceProperties properties) {
        DefaultRequestPreferredIpConfigProvider requestPreferredIpConfigProvider = new DefaultRequestPreferredIpConfigProvider(properties);
        TalbObjectFactory.setDefaultObject(RequestPreferredIpConfigProvider.class, requestPreferredIpConfigProvider);
        return requestPreferredIpConfigProvider;
    }

    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(RequestPreferredNetworkConfigProvider.class)
    public RequestPreferredNetworkConfigProvider requestPreferredNetworkConfigProvider(TalbLoadBalanceProperties properties) {
        DefaultRequestPreferredNetworkConfigProvider requestPreferredNetworkConfigProvider = new DefaultRequestPreferredNetworkConfigProvider(properties);
        TalbObjectFactory.setDefaultObject(RequestPreferredNetworkConfigProvider.class, requestPreferredNetworkConfigProvider);
        return requestPreferredNetworkConfigProvider;
    }


    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(RequestPreferredVersionConfigProvider.class)
    public RequestPreferredVersionConfigProvider requestPreferredVersionConfigProvider(TalbLoadBalanceProperties properties) {
        DefaultRequestPreferredVersionConfigProvider requestPreferredVersionConfigProvider = new DefaultRequestPreferredVersionConfigProvider(properties);
        TalbObjectFactory.setDefaultObject(DefaultRequestPreferredVersionConfigProvider.class, requestPreferredVersionConfigProvider);
        return requestPreferredVersionConfigProvider;
    }

    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(GlobalPreferredNetworkConfigProvider.class)
    public GlobalPreferredNetworkConfigProvider globalPreferredNetworkConfigProvider(TalbLoadBalanceProperties properties) {
        DefaultGlobalPreferredNetworkConfigProvider globalPreferredNetworkConfigProvider = new DefaultGlobalPreferredNetworkConfigProvider(properties);
        TalbObjectFactory.setDefaultObject(GlobalPreferredNetworkConfigProvider.class, globalPreferredNetworkConfigProvider);
        return globalPreferredNetworkConfigProvider;
    }


    @Bean
    @ConditionalOnMissingBean(ConsistentHashLoadBalancerConfigProvider.class)
    public ConsistentHashLoadBalancerConfigProvider consistentHashLoadBalancerConfigProvider() {
        DefaultConsistentHashLoadBalancerConfigProvider loadBalancerConfigProvider = new DefaultConsistentHashLoadBalancerConfigProvider();
        TalbObjectFactory.setDefaultObject(ConsistentHashLoadBalancerConfigProvider.class, loadBalancerConfigProvider);
        return loadBalancerConfigProvider;
    }


    @Bean
    @ConditionalOnBean(RequestPreferredIpConfigProvider.class)
    @ConditionalOnMissingBean(RequestPreferredIpInstanceFilter.class)
    public InstanceFilter requestPreferredInstanceFilter(RequestPreferredIpConfigProvider configProvider) {
        return new RequestPreferredIpInstanceFilter(configProvider);
    }

    @Bean
    @ConditionalOnBean(RequestPreferredNetworkConfigProvider.class)
    @ConditionalOnMissingBean(RequestPreferredNetworkInstanceFilter.class)
    public InstanceFilter requestPreferredNetworkInstanceFilter(RequestPreferredNetworkConfigProvider configProvider) {
        return new RequestPreferredNetworkInstanceFilter(configProvider);
    }


    @Bean
    @ConditionalOnBean(RequestPreferredVersionConfigProvider.class)
    @ConditionalOnMissingBean(RequestPreferredVersionInstanceFilter.class)
    public InstanceFilter requestPreferredVersionInstanceFilter(RequestPreferredVersionConfigProvider configProvider) {
        return new RequestPreferredVersionInstanceFilter(configProvider);
    }

    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(GlobalPreferredNetworkInstanceFilter.class)
    public InstanceFilter globalPreferredNetworkInstanceFilter(GlobalPreferredNetworkConfigProvider configProvider) {
        return new GlobalPreferredNetworkInstanceFilter(configProvider);
    }

    @Bean
    @Primary
    public InstanceFilter compositeInstanceFilter(List<InstanceFilter> filters) {
        return new CompositeInstanceFilter(filters);
    }


    @Bean
    @ConditionalOnBean(ConsistentHashLoadBalancerConfigProvider.class)
    public LoadBalancerProvider loadBalancerProvider(ConsistentHashLoadBalancerConfigProvider configProvider) {
        return () -> new ConsistentHashLoadBalancer(configProvider);
    }


    @Bean
    @ConditionalOnBean(LoadBalancerProvider.class)
    @ConditionalOnMissingBean(LoadBalancer.class)
    public LoadBalancer loadBalancer(InstanceFilter instanceFilter, LoadBalancerProvider loadBalancerProvider) {
        FilterInstanceLoadBalancer loadBalancer = new FilterInstanceLoadBalancer(loadBalancerProvider.get(), instanceFilter);
        TalbObjectFactory.setDefaultObject(LoadBalancer.class, loadBalancer);
        return loadBalancer;
    }
}
