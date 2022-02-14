package jclipper.talb.adapter.spring.cloud.configuration;

import jclipper.talb.adapter.spring.cloud.loadbalancer.TalbReactorLoadBalancer;
import jclipper.talb.base.LoadBalancer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/25 10:18.
 */
@Configuration(proxyBeanMethods = false)
public class TalbDefaultLoadBalancerClientConfiguration {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment,
                                                                                   LoadBalancerClientFactory loadBalancerClientFactory, LoadBalancer loadBalancer) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        ObjectProvider<ServiceInstanceListSupplier> provider = loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class);
        return new TalbReactorLoadBalancer(provider, name, loadBalancer);
    }
}
