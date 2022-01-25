package com.wf2311.talb.factory;

import com.wf2311.talb.factory.base.ConfigProvider;
import com.wf2311.talb.utils.LocalhostUtil;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/20 09:22.
 */
public interface AppInfoConfigProvider extends ConfigProvider {

    /**
     * 当前应用的Host
     *
     * @return
     */
    default String getHost() {
        return LocalhostUtil.getHostName();
    }

    /**
     * 当前应用的IP
     *
     * @return
     */
    default String getIp() {
        return LocalhostUtil.getHostIp();
    }

    /**
     * 当前应用的名称
     *
     * @return
     */
    String getAppName();
}
