package jclipper.talb.factory;

import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.base.ConfigProvider;
import jclipper.talb.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;

/**
 * Talb的attribute中key能否进行透传的配置提供者
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 19:20.
 */
public interface TalbRequestAttributeTransferConfigProvider extends ConfigProvider {
    /**
     * 能够透传的key
     *
     * @return
     */
    Set<String> whiteListKeys();

    /**
     * 不能够透传的key
     *
     * @return
     */
    Set<String> blackListKeys();

    /**
     * filter and get Attributes
     *
     * @param request
     * @return
     */
    default Map<String, String> filteredAttributes(TalbRequest request) {
        if (request == null || request.getAttributes() == null || request.getAttributes().size() == 0) {
            return emptyMap();
        }
        Map<String, Object> map;
        Set<String> whiteListKeys = whiteListKeys();
        if (whiteListKeys != null && whiteListKeys.size() > 0) {
            map = new HashMap<>(whiteListKeys.size());
            for (String key : whiteListKeys) {
                Object value = request.getAttribute(key);
                if (value != null) {
                    map.put(key, value);
                }
            }
        } else {
            map = request.getAttributes();
        }

        Set<String> blackListKeys = blackListKeys();
        if (blackListKeys != null && blackListKeys.size() > 0) {
            for (String key : blackListKeys) {
                map.remove(key);
            }
        }
        if (map.size() == 0) {
            return emptyMap();
        }
        Map<String, String> filteredMap = new HashMap<>(map.size());
        map.forEach((k, v) -> {
            if (v != null) {
                String value;
                if (v instanceof String) {
                    value = (String) v;
                } else {
                    value = JsonUtils.toJsonString(v);
                }
                filteredMap.put(k, value);
            }
        });
        return filteredMap;
    }
}
