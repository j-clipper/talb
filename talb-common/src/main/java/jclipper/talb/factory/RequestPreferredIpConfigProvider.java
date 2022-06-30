package jclipper.talb.factory;

import jclipper.talb.factory.base.ConfigProvider;

/**
 * 配置提供者：是否允许设置偏好访问ServiceInstance的ip
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 10:05.
 */
public interface RequestPreferredIpConfigProvider extends ConfigProvider {

    /**
     * 是否允许设置偏好访问ServiceInstance的ip
     *
     * @return
     */
    boolean isAllowPreferredIp();
}
