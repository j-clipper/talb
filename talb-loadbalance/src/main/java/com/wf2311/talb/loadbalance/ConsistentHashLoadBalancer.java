package com.wf2311.talb.loadbalance;

import com.wf2311.talb.base.Instance;
import com.wf2311.talb.base.LoadBalancer;
import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.factory.ConsistentHashLoadBalancerConfigProvider;
import com.wf2311.talb.request.search.RequestIdSearcher;
import com.wf2311.talb.request.search.impl.DefaultRequestIdSearcher;

import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 一致性哈希负载均衡器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 15:38.
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    private final ConcurrentMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    private final ConsistentHashLoadBalancerConfigProvider configProvider;

    private final RequestIdSearcher requestIdSearcher;

    private final LoadBalancer backupLoadBalancer;

    public ConsistentHashLoadBalancer(ConsistentHashLoadBalancerConfigProvider configProvider, RequestIdSearcher requestIdSearcher) {
        this.configProvider = configProvider;
        this.requestIdSearcher = requestIdSearcher;
        this.backupLoadBalancer = new RoundRobinLoadBalancer();
    }

    public ConsistentHashLoadBalancer(ConsistentHashLoadBalancerConfigProvider configProvider) {
        this(configProvider, new DefaultRequestIdSearcher());
    }

    @Override
    public Instance doChoose(List<Instance> instances, TalbRequest request) {
        List<Instance> sorted = instances.stream().sorted(Comparator.comparingInt(i -> i.id().hashCode())).collect(Collectors.toList());

        String requestType = configProvider.requestTypeParser().apply(request);
        int identityHashCode = getIdentityHashCode(sorted);
        ConsistentHashSelector selector = selectors.get(requestType);
        if (selector == null || selector.identityHashCode != identityHashCode) {
            int virtualNodeNum = configProvider.getVirtualNodeNum() > 0 ? configProvider.getVirtualNodeNum() : 1;
            selectors.put(requestType, new ConsistentHashSelector(instances, identityHashCode, virtualNodeNum));
            selector = selectors.get(requestType);
        }
        String requestId = requestIdSearcher.search(request);

        if (requestId == null) {
            return backupLoadBalancer.choose(instances, request);
        }

        return selector.select(requestId);
    }

    private int getIdentityHashCode(List<Instance> sorted) {
        StringBuilder sb = new StringBuilder();
        for (Instance instance : sorted) {
            sb.append(instance.id());
        }
        return sb.toString().hashCode();
    }


    public static class ConsistentHashSelector {

        /**
         * key表示实例的hash,value表示实例
         */
        private final SortedMap<Integer, Instance> map = new TreeMap<>();

        /**
         * 虚拟节点数量
         */
        private final int virtualNodeNum;

        private final int identityHashCode;

        public ConsistentHashSelector(List<Instance> instances, int identityHashCode, int virtualNodeNum) {
            this.virtualNodeNum = virtualNodeNum;
            for (Instance instance : instances) {
                addInstance(instance);
            }
            this.identityHashCode = identityHashCode;
        }

        public void addInstance(Instance instance) {
            String id = instance.id();
            for (int i = 0; i < virtualNodeNum; i++) {
                int m = getHash(String.format("%s@%d", id, i));
                map.put(m, instance);
            }
        }

        public Instance select(String requestId) {
            return getServerByHashKey(getHash(requestId));
        }

        private Instance getServerByHashKey(int hash) {
            SortedMap<Integer, Instance> subMap = map.tailMap(hash);
            Integer targetServerKey = subMap.isEmpty() ? map.firstKey() : subMap.firstKey();
            return map.get(targetServerKey);
        }

        private static int getHash(String string) {
            final int p = 16777619;
            int hash = (int) 2166136261L;
            for (int i = 0; i < string.length(); i++) {
                hash = (hash ^ string.charAt(i)) * p;
            }
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;

            // 如果算出来的值为负数则取其绝对值
            if (hash < 0) {
                hash = Math.abs(hash);
            }
            return hash;
        }


    }

}
