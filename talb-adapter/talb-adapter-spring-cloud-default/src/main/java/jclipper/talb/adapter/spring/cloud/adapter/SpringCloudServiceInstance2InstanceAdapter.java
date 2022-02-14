package jclipper.talb.adapter.spring.cloud.adapter;

import jclipper.common.utils.CollectionUtils;
import jclipper.talb.base.Instance;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 12:47.
 */
public class SpringCloudServiceInstance2InstanceAdapter implements Instance {

    private final ServiceInstance original;

    public static Instance adapter(ServiceInstance instance) {
        if (instance == null) {
            throw new NullPointerException("ServiceInstance is null");
        }
        return new SpringCloudServiceInstance2InstanceAdapter(instance);
    }


    public static List<Instance> adapters(List<ServiceInstance> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return Collections.emptyList();
        }
        return instances.stream().map(SpringCloudServiceInstance2InstanceAdapter::adapter).collect(Collectors.toList());
    }

    private SpringCloudServiceInstance2InstanceAdapter(ServiceInstance original) {
        this.original = original;
    }

    @Override
    public String getServiceId() {
        return original.getServiceId();
    }

    @Override
    public ServiceInstance original() {
        return original;
    }

    @Override
    public String getHost() {
        return original.getHost();
    }

    @Override
    public Integer getPort() {
        return original.getPort();
    }

    @Override
    public Map<String, String> getMetadata() {
        return original.getMetadata();
    }
}
