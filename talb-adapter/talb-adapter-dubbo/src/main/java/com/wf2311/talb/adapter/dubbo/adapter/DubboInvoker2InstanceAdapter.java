package com.wf2311.talb.adapter.dubbo.adapter;

import com.wf2311.common.utils.CollectionUtils;
import com.wf2311.talb.base.Instance;
import org.apache.dubbo.rpc.Invoker;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/18 09:51.
 */
public class DubboInvoker2InstanceAdapter implements Instance {

    private final Invoker<?> original;

    public static  Instance adapter(Invoker<?> instance) {
        if (instance == null) {
            throw new NullPointerException("Invoker is null");
        }
        return new DubboInvoker2InstanceAdapter(instance);
    }


    public static  List<Instance> adapters(List<Invoker> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return Collections.emptyList();
        }
        return instances.stream().map(DubboInvoker2InstanceAdapter::adapter).collect(Collectors.toList());
    }

    public DubboInvoker2InstanceAdapter(Invoker original) {
        this.original = original;
    }

    @Override
    public String getServiceId() {
        return original.getUrl().getServiceKey();
    }

    @Override
    public Invoker original() {
        return original;
    }

    @Override
    public String getHost() {
        return original.getUrl().getHost();
    }

    @Override
    public Integer getPort() {
        return original.getUrl().getPort();
    }

    @Override
    public Map<String, String> getParameters() {
        return original.getUrl().getParameters();
    }
}
