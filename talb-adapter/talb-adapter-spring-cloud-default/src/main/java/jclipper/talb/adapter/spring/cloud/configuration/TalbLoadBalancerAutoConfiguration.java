package jclipper.talb.adapter.spring.cloud.configuration;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/25 14:35.
 */
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = TalbDefaultLoadBalancerClientConfiguration.class)
public class TalbLoadBalancerAutoConfiguration {
}
