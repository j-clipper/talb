package com.wf2311.talb.adapter.spring.boot.configuration;

import com.wf2311.talb.factory.AppInfoConfigProvider;
import com.wf2311.talb.factory.base.TalbObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/20 09:26.
 */
@Configuration
@ComponentScan
public class TalbConfiguration {
    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public AppInfoConfigProvider appInfoConfigProvider() {
        AppInfoConfigProvider configProvider = () -> appName;
        TalbObjectFactory.setDefaultObject(AppInfoConfigProvider.class, configProvider);
        return configProvider;
    }
}
