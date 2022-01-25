package com.wf2311.talb.loadbalance;

import com.wf2311.talb.base.Instance;
import com.wf2311.talb.base.LoadBalancer;
import com.wf2311.talb.base.TalbRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RoundRobin 轮询负载均衡器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 15:16.
 */
@Slf4j
public class RoundRobinLoadBalancer implements LoadBalancer {

    final AtomicInteger position;

    public RoundRobinLoadBalancer() {
        this(new Random().nextInt(1000));
    }

    public RoundRobinLoadBalancer(int seedPosition) {
        this.position = new AtomicInteger(seedPosition);
    }

    @Override
    public Instance doChoose(List<Instance> instances, TalbRequest request) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No instances available");
            }
            return null;
        }
        if (instances.size() == 1) {
            return instances.get(0);
        }
        int pos = Math.abs(this.position.incrementAndGet());

        return instances.get(pos % instances.size());

    }
}
