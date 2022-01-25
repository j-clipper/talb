package com.wf2311.talb.adapter.spring.cloud.configuration;

import com.wf2311.talb.adapter.spring.boot.configuration.TalbConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 09:11.
 */
@Configuration
@Import({TalbConfiguration.class,TalbLoadBalancerAutoConfiguration.class})
public class TalbSpringCloudCommonConfiguration {


}
