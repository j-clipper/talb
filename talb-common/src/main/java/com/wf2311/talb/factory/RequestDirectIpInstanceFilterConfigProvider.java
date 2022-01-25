package com.wf2311.talb.factory;

import com.wf2311.talb.factory.base.ConfigProvider;

/**
 * RequestDirectIpInstanceFilter 配置提供者
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 10:05.
 */
public interface RequestDirectIpInstanceFilterConfigProvider extends ConfigProvider {

    /**
     * 是否允许设置直接访问ServiceInstance的ip
     *
     * @return
     */
    boolean isAllowDirectIp();
}
