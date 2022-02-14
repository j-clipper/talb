package jclipper.talb.factory;

import jclipper.talb.base.TalbConstants;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.base.ConfigProvider;
import jclipper.talb.utils.JsonUtils;

import java.util.List;
import java.util.function.Function;

/**
 * ConsistentHashLoadBalancer 配置提供者
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 17:32.
 */
public interface ConsistentHashLoadBalancerConfigProvider extends ConfigProvider {

    /**
     * 虚拟节点的数量，默认为16
     *
     * @return
     */
    default int getVirtualNodeNum() {
        return 16;
    }

    /**
     * requestType解析器，requestType用于ConsistentHashLoadBalancer.selectors的key
     *
     * @return
     */
    default Function<TalbRequest, String> requestTypeParser() {
        return request -> {
            String serviceId = request.getServiceId();
            List<String> markedList = (List<String>) request.getAttribute(TalbConstants.INSTANCE_FILTER_MARKED_KEY);
            String marked = markedList == null ? "" : JsonUtils.toJsonString(markedList);
            return serviceId + marked;
        };
    }

}
