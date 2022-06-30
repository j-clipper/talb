package jclipper.talb.factory;

import jclipper.talb.factory.base.ConfigProvider;

/**
 * 配置提供者:是否允许优先选择ServiceInstance网段
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 10:08.
 */
public interface RequestPreferredNetworkConfigProvider extends ConfigProvider {

    /**
     * 是否允许优先选择ServiceInstance网段
     *
     * @return 是否允许优先选择ServiceInstance网段
     */
    boolean isAllowPreferredNetworks();
}
