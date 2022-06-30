package jclipper.talb.adapter.spring.boot.factory;

import jclipper.talb.adapter.spring.boot.configuration.TalbLoadBalanceProperties;
import jclipper.talb.factory.RequestPreferredNetworkConfigProvider;

/**
 * 默认的{@link RequestPreferredNetworkConfigProvider} ，从{@link TalbLoadBalanceProperties}中获取配置
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 11:16.
 */
public class DefaultRequestPreferredNetworkConfigProvider implements RequestPreferredNetworkConfigProvider {

    private final TalbLoadBalanceProperties properties;

    public DefaultRequestPreferredNetworkConfigProvider(TalbLoadBalanceProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isAllowPreferredNetworks() {
        return properties.getRequest() != null && properties.getRequest().isAllowPreferredNetwork();
    }
}
