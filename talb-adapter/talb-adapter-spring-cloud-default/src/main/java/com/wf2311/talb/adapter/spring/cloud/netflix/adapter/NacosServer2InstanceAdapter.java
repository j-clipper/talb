package com.wf2311.talb.adapter.spring.cloud.netflix.adapter;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.Server;
import com.wf2311.common.utils.CollectionUtils;
import com.wf2311.talb.base.Instance;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 20:37.
 */
public class NacosServer2InstanceAdapter implements Instance {
    private final NacosServer original;
    private final com.alibaba.nacos.api.naming.pojo.Instance instance;


    public static Instance adapter(Server server) {
        if (server == null) {
            throw new NullPointerException("ServiceInstance is null");
        }
        return new NacosServer2InstanceAdapter(server);
    }

    public static List<Instance> adapters(List<Server> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return Collections.emptyList();
        }
        return instances.stream().map(NacosServer2InstanceAdapter::adapter).collect(Collectors.toList());
    }

    private NacosServer2InstanceAdapter(Server original) {
        if (!(original instanceof NacosServer)) {
            throw new IllegalArgumentException("original type is not com.alibaba.cloud.nacos.ribbon.NacosServer");
        }
        this.original = (NacosServer) original;
        this.instance = this.original.getInstance();
    }

    @Override
    public String getServiceId() {
        return instance.getInstanceId();
    }

    @Override
    public NacosServer original() {
        return original;
    }

    @Override
    public String getHost() {
        return instance.getIp();
    }

    @Override
    public Integer getPort() {
        return instance.getPort();
    }

    @Override
    public Map<String, String> getMetadata() {
        return instance.getMetadata();
    }
}
