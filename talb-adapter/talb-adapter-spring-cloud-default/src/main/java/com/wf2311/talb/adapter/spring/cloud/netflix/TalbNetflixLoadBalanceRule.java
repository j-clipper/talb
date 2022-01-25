package com.wf2311.talb.adapter.spring.cloud.netflix;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.wf2311.talb.adapter.spring.cloud.netflix.adapter.NacosServer2InstanceAdapter;
import com.wf2311.talb.adapter.spring.cloud.netflix.adapter.NetflixServer2InstanceAdapter;
import com.wf2311.talb.base.Instance;
import com.wf2311.talb.base.LoadBalancer;
import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.context.TalbContext;

import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 17:25.
 */
public class TalbNetflixLoadBalanceRule implements IRule {
    private final LoadBalancer loadBalancer;
    private ILoadBalancer lb;

    public TalbNetflixLoadBalanceRule(LoadBalancer loadBalancer, ILoadBalancer lb) {
        this.loadBalancer = loadBalancer;
        this.lb = lb;
    }

    @Override
    public Server choose(Object key) {
        List<Server> servers = lb.getAllServers();

        if (servers == null || servers.isEmpty()) {
            return null;
        }
        Server server = servers.get(0);

        boolean isNacosServer = server instanceof NacosServer;

        List<Instance> instances = isNacosServer ? NacosServer2InstanceAdapter.adapters(servers) :
                NetflixServer2InstanceAdapter.adapters(servers);

        //FIXME:TalbRequest需要预先设置并缓存进TalbContext

        TalbRequest request = TalbContext.getLocalContext() == null ? null : TalbContext.getLocalContext().request();
        Instance choose = loadBalancer.choose(instances, request);

        return (Server) choose.original();
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        this.lb = lb;
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return lb;
    }
}
