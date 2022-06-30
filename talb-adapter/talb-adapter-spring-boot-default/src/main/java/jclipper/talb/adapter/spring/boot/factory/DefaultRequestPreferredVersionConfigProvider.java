package jclipper.talb.adapter.spring.boot.factory;

import jclipper.talb.adapter.spring.boot.configuration.TalbLoadBalanceProperties;
import jclipper.talb.factory.RequestPreferredVersionConfigProvider;

/**
 * 默认的 {@link RequestPreferredVersionConfigProvider}
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/2/22 10:03.
 */
public class DefaultRequestPreferredVersionConfigProvider implements RequestPreferredVersionConfigProvider {

    private final TalbLoadBalanceProperties properties;

    public DefaultRequestPreferredVersionConfigProvider(TalbLoadBalanceProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isAllowPreferredVersion() {
        return properties.getRequest() != null && properties.getRequest().isAllowPreferredVersion();
    }
}
