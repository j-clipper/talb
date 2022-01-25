package com.wf2311.talb.adapter.spring.cloud.web.configuration;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.wf2311.talb.adapter.spring.cloud.netflix.TalbNetflixLoadBalanceRule;
import com.wf2311.talb.base.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 21:12.
 */
@Configuration
@Slf4j
public class TalbSpringCloudNetflixLoadBalanceConfiguration {

    @Bean
    @ConditionalOnBean(ILoadBalancer.class)
    public TalbNetflixLoadBalanceRule talbNetflixLoadBalanceRule(LoadBalancer loadBalancer, ILoadBalancer lb) {
        log.info("init TalbNetflixLoadBalanceRule");
        return new TalbNetflixLoadBalanceRule(loadBalancer, lb);
    }
}
