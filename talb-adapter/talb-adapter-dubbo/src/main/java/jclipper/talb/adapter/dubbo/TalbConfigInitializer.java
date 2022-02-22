package jclipper.talb.adapter.dubbo;

import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.LoadBalancer;
import jclipper.talb.factory.ConsistentHashLoadBalancerConfigProvider;
import jclipper.talb.factory.GlobalPreferredNetworkConfigProvider;
import jclipper.talb.factory.RequestPreferredIpConfigProvider;
import jclipper.talb.factory.RequestPreferredNetworkConfigProvider;
import jclipper.talb.factory.base.TalbObjectFactory;
import jclipper.talb.loadbalance.filter.CompositeInstanceFilter;
import jclipper.talb.loadbalance.filter.GlobalPreferredNetworkInstanceFilter;
import jclipper.talb.loadbalance.filter.RequestPreferredIpInstanceFilter;
import jclipper.talb.loadbalance.filter.RequestPreferredNetworkInstanceFilter;
import jclipper.talb.loadbalance.ConsistentHashLoadBalancer;
import jclipper.talb.loadbalance.FilterInstanceLoadBalancer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/19 17:40.
 */
public class TalbConfigInitializer {


    public static LoadBalancer initLoadBalancer() {
        RequestPreferredIpInstanceFilter requestPreferredIpInstanceFilter = new RequestPreferredIpInstanceFilter(TalbObjectFactory.getDefaultObject(RequestPreferredIpConfigProvider.class));
        RequestPreferredNetworkInstanceFilter requestPreferredNetworkInstanceFilter = new RequestPreferredNetworkInstanceFilter(TalbObjectFactory.getDefaultObject(RequestPreferredNetworkConfigProvider.class));
        GlobalPreferredNetworkInstanceFilter globalPreferredNetworkInstanceFilter = new GlobalPreferredNetworkInstanceFilter(TalbObjectFactory.getDefaultObject(GlobalPreferredNetworkConfigProvider.class));
        List<InstanceFilter> filters = new ArrayList<>();
        filters.add(requestPreferredIpInstanceFilter);
        filters.add(requestPreferredNetworkInstanceFilter);
        filters.add(globalPreferredNetworkInstanceFilter);

        CompositeInstanceFilter instanceFilter = new CompositeInstanceFilter(filters);

        ConsistentHashLoadBalancer consistentHashLoadBalancer = new ConsistentHashLoadBalancer(TalbObjectFactory.getDefaultObject(ConsistentHashLoadBalancerConfigProvider.class));
        return new FilterInstanceLoadBalancer(consistentHashLoadBalancer, instanceFilter);
    }
}
