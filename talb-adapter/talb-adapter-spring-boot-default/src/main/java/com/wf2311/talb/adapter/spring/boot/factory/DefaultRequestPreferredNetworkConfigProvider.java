package com.wf2311.talb.adapter.spring.boot.factory;

import com.wf2311.talb.adapter.spring.boot.configuration.TalbLoadBalanceProperties;
import com.wf2311.talb.factory.RequestPreferredNetworkInstanceFilterConfigProvider;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 11:16.
 */
public class DefaultRequestPreferredNetworkConfigProvider implements RequestPreferredNetworkInstanceFilterConfigProvider {

    private final TalbLoadBalanceProperties properties;

    public DefaultRequestPreferredNetworkConfigProvider(TalbLoadBalanceProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isAllowPreferredNetworks() {
        return properties.getRequest() != null && properties.getRequest().isAllowPreferredNetworks();
    }
}
