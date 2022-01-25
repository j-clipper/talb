package com.wf2311.talb.adapter.spring.boot.factory;

import com.wf2311.talb.adapter.spring.boot.configuration.TalbLoadBalanceProperties;
import com.wf2311.talb.factory.RequestDirectIpInstanceFilterConfigProvider;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 11:03.
 */
public class DefaultRequestDirectIpConfigProvider implements RequestDirectIpInstanceFilterConfigProvider {
    private final TalbLoadBalanceProperties properties;

    public DefaultRequestDirectIpConfigProvider(TalbLoadBalanceProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isAllowDirectIp() {
        return properties.getRequest() != null && properties.getRequest().isAllowDirectIp();
    }
}
