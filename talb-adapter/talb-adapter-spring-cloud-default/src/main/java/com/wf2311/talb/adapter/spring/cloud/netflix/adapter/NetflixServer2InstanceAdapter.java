package com.wf2311.talb.adapter.spring.cloud.netflix.adapter;

import com.netflix.loadbalancer.Server;
import com.wf2311.common.utils.CollectionUtils;
import com.wf2311.talb.base.Instance;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 20:18.
 */
public class NetflixServer2InstanceAdapter implements Instance {

    private final Server original;


    public static Instance adapter(Server server) {
        if (server == null) {
            throw new NullPointerException("ServiceInstance is null");
        }
        return new NetflixServer2InstanceAdapter(server);
    }

    public static List<Instance> adapters(List<Server> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return Collections.emptyList();
        }
        return instances.stream().map(NetflixServer2InstanceAdapter::adapter).collect(Collectors.toList());
    }

    private NetflixServer2InstanceAdapter(Server original) {
        this.original = original;
    }

    @Override
    public String getServiceId() {
        return original.getId();
    }

    @Override
    public Server original() {
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
}
