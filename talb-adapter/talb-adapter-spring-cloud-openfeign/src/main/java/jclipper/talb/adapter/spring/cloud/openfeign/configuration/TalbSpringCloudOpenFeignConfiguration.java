package jclipper.talb.adapter.spring.cloud.openfeign.configuration;

import jclipper.talb.trace.feign.TalbTraceFeignRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/24 16:18.
 */
@Configuration
@ConditionalOnClass(name = {"feign.RequestInterceptor"})
public class TalbSpringCloudOpenFeignConfiguration {

    @Bean
    public TalbTraceFeignRequestInterceptor talbTraceFeignRequestInterceptor() {
        return new TalbTraceFeignRequestInterceptor();
    }

}
