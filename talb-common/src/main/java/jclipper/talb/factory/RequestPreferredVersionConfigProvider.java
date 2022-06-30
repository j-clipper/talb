package jclipper.talb.factory;

import jclipper.talb.factory.base.ConfigProvider;

/**
 * 配置提供者:是否允许优先选择ServiceInstance的版本号
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 10:08.
 */
public interface RequestPreferredVersionConfigProvider extends ConfigProvider {

    /**
     * 是否允许优先选择ServiceInstance的版本号
     *
     * @return
     */
    boolean isAllowPreferredVersion();
}
