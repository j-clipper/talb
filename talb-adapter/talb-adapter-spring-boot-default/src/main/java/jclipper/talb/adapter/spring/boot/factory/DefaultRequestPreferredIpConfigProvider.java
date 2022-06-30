package jclipper.talb.adapter.spring.boot.factory;

import jclipper.talb.adapter.spring.boot.configuration.TalbLoadBalanceProperties;
import jclipper.talb.factory.RequestPreferredIpConfigProvider;

/**
 * 默认的{@link RequestPreferredIpConfigProvider}，从{@link TalbLoadBalanceProperties}中获取配置
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 11:03.
 */
public class DefaultRequestPreferredIpConfigProvider implements RequestPreferredIpConfigProvider {
    private final TalbLoadBalanceProperties properties;

    public DefaultRequestPreferredIpConfigProvider(TalbLoadBalanceProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isAllowPreferredIp() {
        return properties.getRequest() != null && properties.getRequest().isAllowPreferredIp();
    }
}
