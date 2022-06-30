package jclipper.talb.base;

import java.net.URI;
import java.util.Map;

/**
 * Talb请求 ，记录请求的相关信息
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:13.
 */
public interface TalbRequest {

    /**
     * 请求中携带的直接访问ServiceInstance的ip的key，会依次在Header、Cookie、QueryParam中进行查找
     */
    String PREFERRED_IP_KEY = "_g_preferred_ip";

    /**
     * 请求中携带的优先选择ServiceInstance网段的key，会依次在Header、Cookie、QueryParam中进行查找
     */
    String PREFERRED_NETWORK_KEY = "_g_preferred_network";


    /**
     * 请求中携带的优先选择ServiceInstance版本号的解压，会依次在Header、Cookie、QueryParam中进行查找
     */
    String PREFERRED_VERSION_KEY = "_g_service_version";

    /**
     * 请求中携带的requestId的key，会依次在Header、Cookie、Attributes中进行查找
     */
    String REQUEST_ID_KEY = "_g_request_id";


    /**
     * 服务ID
     *
     * @return 服务ID
     */
    String getServiceId();


    /**
     * 获取url
     *
     * @return 请求的url
     */
    String getUrl();

    /**
     * 获取URI
     *
     * @return 请求的URI
     */
    URI getUri();

    /**
     * 获取URI中携带的参数
     *
     * @return 请求的URI中携带的参数
     */
    Map<String, String> getQueryParams();

    /**
     * 获取请求头
     *
     * @return 请求头
     */
    Map<String, String> getHeaders();

    /**
     * 获取cookie
     *
     * @return cookie
     */
    Map<String, String> getCookies();

    /**
     * 额外属性
     *
     * @return
     */
    Map<String, Object> getAttributes();


    /**
     * 获取url查询参数值
     *
     * @param key 参数名
     * @return 参数值
     */
    default String getQueryParam(String key) {
        if (getQueryParams() != null) {
            return getQueryParams().get(key);
        }
        return null;
    }

    /**
     * 获取请求头值
     *
     * @param key 参数名
     * @return 请求头值
     */
    default String getHeader(String key) {
        if (getHeaders() != null) {
            return getHeaders().get(key);
        }
        return null;
    }

    /**
     * 获取cookie值
     *
     * @param key 参数名
     * @return cookie值
     */
    default String getCookie(String key) {
        if (getHeaders() != null) {
            return getCookies().get(key);
        }
        return null;
    }

    /**
     * 获取属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    default Object getAttribute(String key) {
        if (getAttributes() != null) {
            return getAttributes().get(key);
        }
        return null;
    }

    /**
     * 设置属性值
     *
     * @param key   属性名
     * @param value 属性值
     */
    default void putAttribute(String key, Object value) {
        if (getAttributes() == null) {
            throw new NullPointerException("TalbRequest.attributes is null");
        }
        getAttributes().put(key, value);
    }
}
