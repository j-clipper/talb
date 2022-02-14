package jclipper.talb.adapter.spring.boot.factory;


import jclipper.talb.adapter.spring.boot.configuration.TalbLoadBalanceProperties;
import jclipper.talb.factory.GlobalPreferredNetworkInstanceFilterConfigProvider;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * 默认的GlobalPreferredNetworkInstanceFilterConfigProvider
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 11:17.
 */
public class DefaultGlobalPreferredNetworkConfigProvider implements GlobalPreferredNetworkInstanceFilterConfigProvider {
    private final TalbLoadBalanceProperties properties;

    public DefaultGlobalPreferredNetworkConfigProvider(TalbLoadBalanceProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isEnabled() {
        return properties.getTestEnv() != null && properties.getTestEnv().isEnabled();
    }

    @Override
    public Set<String> getAllowNetworks() {
        TalbLoadBalanceProperties.TestEnv testEnv = properties.getTestEnv();
        if (testEnv == null) {
            return emptySet();
        }
        return testEnv.getAllowNetworks();
    }

    @Override
    public Set<String> getDisableNetworks() {
        TalbLoadBalanceProperties.TestEnv testEnv = properties.getTestEnv();
        if (testEnv == null) {
            return emptySet();
        }
        return testEnv.getDisableNetworks();
    }
}
