package com.wf2311.talb.adapter.spring.cloud.web.configuration;

import com.wf2311.talb.adapter.spring.cloud.configuration.TalbSpringCloudCommonConfiguration;
import com.wf2311.talb.trace.spring.web.TalbWebMvcConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 15:55.
 */
@Configuration
@Import(TalbSpringCloudCommonConfiguration.class)
@ComponentScan
public class TalbSpringCloudAutoConfiguration {

    @Bean
    public TalbWebMvcConfigurer talbWebMvcConfigurer() {
        return new TalbWebMvcConfigurer();
    }
}
