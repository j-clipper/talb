package jclipper.talb.utils;


import com.google.common.base.Strings;
import jclipper.talb.base.Instance;
import jclipper.talb.base.TalbRequest;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static jclipper.talb.base.TalbRequest.*;


/**
 * 请求工具类
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 11:53.
 */
public class RequestUtils {

    public static final String ITERABLE_TO_STRING_SPLIT = ",";

    private static final String JSON_ARRAY_START_SIGN = "[";
    private static final String JSON_ARRAY_END_SIGN = "]";

    /**
     * 请求中携带的直接访问Instance的ip的值，会依次在Header、Cookie、QueryParam中进行查找
     *
     * @param request
     * @return
     */
    public static Set<String> findPreferredIpAddress(TalbRequest request) {
        return findSetValue(request, PREFERRED_IP_KEY);
    }

    /**
     * 请求中携带的优先选择Instance网段的值，先在Attribute中查找，如果不存在再依次在Header、Cookie、QueryParam中进行查找，多个之间用英文逗号进行分隔
     *
     * @param request
     * @return
     */
    public static Set<String> findPreferredNetworks(TalbRequest request) {
        return findSetValue(request, PREFERRED_NETWORK_KEY);
    }


    /**
     * 请求中携带的直接访问Instance的版本号的值，会依次在Header、Cookie、QueryParam中进行查找
     *
     * @param request
     * @return
     */
    public static Set<String> findPreferredVersions(TalbRequest request) {
        return findSetValue(request, PREFERRED_VERSION_KEY);
    }

    public static Set<String> findSetValue(TalbRequest request, String key) {
        Object exist = findAttributeValue(request, key);
        if (exist != null) {
            if (exist instanceof Set) {
                return (Set<String>) exist;
            }
            if (exist instanceof String) {
                return parseSet((String) exist);
            }
        }
        String value = findKeyValueInHeaderAndCookieAndQueryParam(request, key);
        if (Strings.isNullOrEmpty(value)) {
            return Collections.emptySet();
        }
        return parseSet(value);
    }

    private static Set<String> parseSet(String value) {
        if (value.startsWith(JSON_ARRAY_START_SIGN) && value.endsWith(JSON_ARRAY_END_SIGN)) {
            return new HashSet<>(JsonUtils.parseArray(value, String.class));
        }
        return Arrays.stream(value.split(ITERABLE_TO_STRING_SPLIT)).filter(s -> !Strings.isNullOrEmpty(s)).collect(Collectors.toSet());
    }

    /**
     * 请求中携带的优先选择Instance网段的值，先在Attribute中查找，如果不存在再依次在Header、Cookie、QueryParam中进行查找，多个之间用英文逗号进行分隔
     *
     * @param request
     * @return
     */
    public static String findRequestId(TalbRequest request) {
        String value = (String) findAttributeValue(request, REQUEST_ID_KEY);
        if (!Strings.isNullOrEmpty(value)) {
            return value;
        }
        return findKeyValueInHeaderAndCookie(request, REQUEST_ID_KEY);
    }


    @SuppressWarnings("unchecked")
    private static String findKeyValueInHeaderAndCookieAndQueryParam(TalbRequest request, String key) {
        return filterFirst(s -> !Strings.isNullOrEmpty(s)
                , () -> findHeaderValue(request, key)
                , () -> findCookieValue(request, key), () -> findQueryParamValue(request, key));
    }

    @SuppressWarnings("unchecked")
    private static String findKeyValueInHeaderAndCookie(TalbRequest request, String key) {
        return filterFirst(s -> !Strings.isNullOrEmpty(s)
                , () -> findHeaderValue(request, key)
                , () -> findCookieValue(request, key));
    }


    private static String findHeaderValue(TalbRequest request, String key) {
        if (request == null || request.getHeaders() == null) {
            return null;
        }
        return request.getHeader(key);
    }


    private static String findCookieValue(TalbRequest request, String key) {
        if (request == null || request.getCookies() == null) {
            return null;
        }
        return request.getCookie(key);
    }

    private static String findQueryParamValue(TalbRequest request, String key) {
        if (request == null || request.getUri() == null) {
            return null;
        }
        return request.getQueryParam(key);
    }

    private static Object findAttributeValue(TalbRequest request, String key) {
        if (request == null || request.getAttributes() == null) {
            return null;
        }
        Object v = request.getAttributes().get(key);
        if (v == null) {
            return null;
        }
        return v;
    }

    /**
     * 判断服务实例的ip是不是在指定的网段里面，匹配规则：正则匹配或前缀匹配
     *
     * @param instance          实例
     * @param preferredNetworks 首选网络
     * @return true/false
     */
    public static boolean isMatchAnyNetwork(Instance instance, Set<String> preferredNetworks) {
        return preferredNetworks.stream().anyMatch(p -> isNetworkMatch(instance, p));
    }

    /**
     * 判断服务实例的ip是不是属于指定的网段，匹配规则：正则匹配或前缀匹配
     *
     * @param instance         实例
     * @param preferredNetwork 首选网络
     * @return true/false
     */
    public static boolean isNetworkMatch(Instance instance, String preferredNetwork) {
        final String hostAddress = instance.getHost();
        return hostAddress.matches(preferredNetwork) || hostAddress.startsWith(preferredNetwork);
    }

    @SafeVarargs
    private static <T> T filterFirst(Predicate<T> predicate, Supplier<T>... suppliers) {
        for (Supplier<T> supplier : suppliers) {
            if (supplier == null) {
                continue;
            }
            T v = supplier.get();
            if (predicate == null) {
                return v;
            }
            if (predicate.test(v)) {
                return v;
            }
        }
        return null;
    }

    private static <T> List<T> filterAll(Predicate<T> predicate, Supplier<T>... suppliers) {
        List<T> result = new ArrayList<>();
        for (Supplier<T> supplier : suppliers) {
            if (supplier == null) {
                continue;
            }
            T v = supplier.get();
            if (predicate == null) {
                result.add(v);
                continue;
            }
            if (predicate.test(v)) {
                result.add(v);
            }
        }
        return result;
    }
}
