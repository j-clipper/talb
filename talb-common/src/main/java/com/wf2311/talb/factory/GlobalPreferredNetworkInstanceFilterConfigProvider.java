package com.wf2311.talb.factory;

import com.wf2311.talb.factory.base.ConfigProvider;

import java.util.Set;

/**
 * GlobalPreferredNetworkInstanceFilter 配置提供者
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 10:12.
 */
public interface GlobalPreferredNetworkInstanceFilterConfigProvider  extends ConfigProvider {
    /**
     * 是否启用
     *
     * @return
     */
    boolean isEnabled();

    /**
     * 允许ServiceInstance的网段集合
     *
     * @return
     */
    Set<String> getAllowNetworks();

    /**
     * 禁用ServiceInstance的网段集合
     *
     * @return
     */
    Set<String> getDisableNetworks();
}
