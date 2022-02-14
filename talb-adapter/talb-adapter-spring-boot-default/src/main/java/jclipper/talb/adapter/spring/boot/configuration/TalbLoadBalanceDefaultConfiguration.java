package jclipper.talb.adapter.spring.boot.configuration;

import jclipper.talb.adapter.spring.boot.factory.DefaultGlobalPreferredNetworkConfigProvider;
import jclipper.talb.adapter.spring.boot.factory.DefaultRequestDirectIpConfigProvider;
import jclipper.talb.adapter.spring.boot.factory.DefaultRequestPreferredNetworkConfigProvider;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.LoadBalancer;
import jclipper.talb.factory.*;
import jclipper.talb.factory.base.TalbObjectFactory;
import jclipper.talb.factory.internal.DefaultConsistentHashLoadBalancerConfigProvider;
import jclipper.talb.loadbalance.filter.CompositeInstanceFilter;
import jclipper.talb.loadbalance.filter.GlobalPreferredNetworkInstanceFilter;
import jclipper.talb.loadbalance.filter.RequestDirectIpInstanceFilter;
import jclipper.talb.loadbalance.filter.RequestPreferredNetworkInstanceFilter;
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
    @ConditionalOnMissingBean(RequestDirectIpInstanceFilterConfigProvider.class)
    public RequestDirectIpInstanceFilterConfigProvider requestDirectIpInstanceFilterConfigProvider(TalbLoadBalanceProperties properties) {
        DefaultRequestDirectIpConfigProvider requestDirectIpConfigProvider = new DefaultRequestDirectIpConfigProvider(properties);
        TalbObjectFactory.setDefaultObject(RequestDirectIpInstanceFilterConfigProvider.class, requestDirectIpConfigProvider);
        return requestDirectIpConfigProvider;
    }

    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(RequestPreferredNetworkInstanceFilterConfigProvider.class)
    public RequestPreferredNetworkInstanceFilterConfigProvider requestPreferredNetworkInstanceFilterConfigProvider(TalbLoadBalanceProperties properties) {
        DefaultRequestPreferredNetworkConfigProvider requestPreferredNetworkConfigProvider = new DefaultRequestPreferredNetworkConfigProvider(properties);
        TalbObjectFactory.setDefaultObject(RequestPreferredNetworkInstanceFilterConfigProvider.class, requestPreferredNetworkConfigProvider);
        return requestPreferredNetworkConfigProvider;
    }

    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(GlobalPreferredNetworkInstanceFilterConfigProvider.class)
    public GlobalPreferredNetworkInstanceFilterConfigProvider globalPreferredNetworkInstanceFilterConfigProvider(TalbLoadBalanceProperties properties) {
        DefaultGlobalPreferredNetworkConfigProvider globalPreferredNetworkConfigProvider = new DefaultGlobalPreferredNetworkConfigProvider(properties);
        TalbObjectFactory.setDefaultObject(GlobalPreferredNetworkInstanceFilterConfigProvider.class, globalPreferredNetworkConfigProvider);
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
    @ConditionalOnBean(RequestDirectIpInstanceFilterConfigProvider.class)
    @ConditionalOnMissingBean(RequestDirectIpInstanceFilter.class)
    public InstanceFilter requestDirectIpInstanceFilter(RequestDirectIpInstanceFilterConfigProvider configProvider) {
        return new RequestDirectIpInstanceFilter(configProvider);
    }

    @Bean
    @ConditionalOnBean(RequestPreferredNetworkInstanceFilterConfigProvider.class)
    @ConditionalOnMissingBean(RequestPreferredNetworkInstanceFilter.class)
    public InstanceFilter requestPreferredNetworkServiceInstanceFilter(RequestPreferredNetworkInstanceFilterConfigProvider configProvider) {
        return new RequestPreferredNetworkInstanceFilter(configProvider);
    }

    @Bean
    @ConditionalOnBean(TalbLoadBalanceProperties.class)
    @ConditionalOnMissingBean(GlobalPreferredNetworkInstanceFilter.class)
    public InstanceFilter globalPreferredNetworkServiceInstanceFilter(GlobalPreferredNetworkInstanceFilterConfigProvider configProvider) {
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
