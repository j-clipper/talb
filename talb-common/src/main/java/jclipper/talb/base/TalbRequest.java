package jclipper.talb.base;

import java.net.URI;
import java.util.Map;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:13.
 */
public interface TalbRequest {

    /**
     * 请求中携带的直接访问ServiceInstance的ip的key，会依次在Header、Cookie、QueryParam中进行查找
     */
    String DIRECT_IP_KEY = "_g_direct_ip";

    /**
     * 请求中携带的优先选择ServiceInstance网段的key，会依次在Header、Cookie、QueryParam中进行查找
     */
    String PREFERRED_NETWORK_KEY = "_g_preferred_networks";


    /**
     * 请求中携带的requestId的key，会依次在Header、Cookie、Attributes中进行查找
     */
    String REQUEST_ID_KEY = "_g_request_id";

    /**
     * serviceId
     *
     * @return
     */
    String getServiceId();


    /**
     * 获取url
     *
     * @return
     */
    String getUrl();

    /**
     * 获取URI
     *
     * @return
     */
    URI getUri();

    /**
     * 获取URI中携带的参数
     *
     * @return
     */
    Map<String, String> getQueryParams();

    /**
     * 请求头
     *
     * @return
     */
    Map<String, String> getHeaders();

    /**
     * cookie
     *
     * @return
     */
    Map<String, String> getCookies();

    /**
     * 额外属性
     *
     * @return
     */
    Map<String, Object> getAttributes();


    /**
     * @param name
     * @return
     */
    default String getQueryParam(String name) {
        if (getQueryParams() != null) {
            return getQueryParams().get(name);
        }
        return null;
    }

    default String getHeader(String name) {
        if (getHeaders() != null) {
            return getHeaders().get(name);
        }
        return null;
    }


    default String getCookie(String name) {
        if (getHeaders() != null) {
            return getCookies().get(name);
        }
        return null;
    }

    default Object getAttribute(String name) {
        if (getAttributes() != null) {
            return getAttributes().get(name);
        }
        return null;
    }

    default void putAttribute(String name, Object value) {
        if (getAttributes() == null) {
            throw new NullPointerException("TalbRequest.attributes is null");
        }
        getAttributes().put(name, value);
    }
}
