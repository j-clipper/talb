package com.wf2311.talb.base;

import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * 服务实例
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 20:59.
 */
public interface Instance {

    /**
     * serviceId
     *
     * @return
     */
    String getServiceId();

    /**
     * 原始对象
     *
     * @return
     */
    Object original();

    /**
     * ip
     *
     * @return
     */
    String getHost();

    /**
     * 端口
     *
     * @return
     */
    Integer getPort();

    /**
     * url 参数
     *
     * @return
     */
    default Map<String, String> getParameters() {
        return emptyMap();
    }

    /**
     * 元数据
     *
     * @return
     */
    default Map<String, String> getMetadata() {
        return emptyMap();
    }

    ;

    /**
     * ID
     *
     * @return
     */
    default String id() {
        return getHost() + ":" + getPort();
    }
}
