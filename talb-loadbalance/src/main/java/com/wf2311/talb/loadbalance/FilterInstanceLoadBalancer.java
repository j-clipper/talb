package com.wf2311.talb.loadbalance;

import com.wf2311.talb.base.Instance;
import com.wf2311.talb.base.InstanceFilter;
import com.wf2311.talb.base.LoadBalancer;
import com.wf2311.talb.base.TalbRequest;

import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 19:12.
 */
public class FilterInstanceLoadBalancer implements LoadBalancer {

    private final LoadBalancer loadBalancer;

    private final InstanceFilter instanceFilter;

    public FilterInstanceLoadBalancer(LoadBalancer loadBalancer, InstanceFilter instanceFilter) {
        this.loadBalancer = loadBalancer;
        this.instanceFilter = instanceFilter;
    }

    @Override
    public Instance doChoose(List<Instance> instances, TalbRequest request) {
        if (instanceFilter != null) {
            instances = instanceFilter.filter(instances, request);
        }
        return loadBalancer.choose(instances, request);
    }


}
