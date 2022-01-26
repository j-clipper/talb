package com.wf2311.talb.adapter.dubbo;

import com.wf2311.talb.base.InstanceFilter;
import com.wf2311.talb.base.LoadBalancer;
import com.wf2311.talb.factory.ConsistentHashLoadBalancerConfigProvider;
import com.wf2311.talb.factory.GlobalPreferredNetworkInstanceFilterConfigProvider;
import com.wf2311.talb.factory.RequestDirectIpInstanceFilterConfigProvider;
import com.wf2311.talb.factory.RequestPreferredNetworkInstanceFilterConfigProvider;
import com.wf2311.talb.factory.base.TalbObjectFactory;
import com.wf2311.talb.loadbalance.filter.CompositeInstanceFilter;
import com.wf2311.talb.loadbalance.filter.GlobalPreferredNetworkInstanceFilter;
import com.wf2311.talb.loadbalance.filter.RequestDirectIpInstanceFilter;
import com.wf2311.talb.loadbalance.filter.RequestPreferredNetworkInstanceFilter;
import com.wf2311.talb.loadbalance.ConsistentHashLoadBalancer;
import com.wf2311.talb.loadbalance.FilterInstanceLoadBalancer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/19 17:40.
 */
public class TalbConfigInitializer {


    public static LoadBalancer initLoadBalancer() {
        RequestDirectIpInstanceFilter requestDirectIpInstanceFilter = new RequestDirectIpInstanceFilter(TalbObjectFactory.getDefaultObject(RequestDirectIpInstanceFilterConfigProvider.class));
        RequestPreferredNetworkInstanceFilter requestPreferredNetworkInstanceFilter = new RequestPreferredNetworkInstanceFilter(TalbObjectFactory.getDefaultObject(RequestPreferredNetworkInstanceFilterConfigProvider.class));
        GlobalPreferredNetworkInstanceFilter globalPreferredNetworkInstanceFilter = new GlobalPreferredNetworkInstanceFilter(TalbObjectFactory.getDefaultObject(GlobalPreferredNetworkInstanceFilterConfigProvider.class));
        List<InstanceFilter> filters = new ArrayList<>();
        filters.add(requestDirectIpInstanceFilter);
        filters.add(requestPreferredNetworkInstanceFilter);
        filters.add(globalPreferredNetworkInstanceFilter);

        CompositeInstanceFilter instanceFilter = new CompositeInstanceFilter(filters);

        ConsistentHashLoadBalancer consistentHashLoadBalancer = new ConsistentHashLoadBalancer(TalbObjectFactory.getDefaultObject(ConsistentHashLoadBalancerConfigProvider.class));
        return new FilterInstanceLoadBalancer(consistentHashLoadBalancer, instanceFilter);
    }
}
