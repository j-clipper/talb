package jclipper.talb.loadbalance.filter;

import jclipper.common.utils.CollectionUtils;
import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.Ordered;
import jclipper.talb.base.TalbRequest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * 服务实例过滤器：组合过滤器，按照 {@link CompositeInstanceFilter#delegates }中的顺序依次对服务实例进行过滤
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 15:45.
 */
public class CompositeInstanceFilter implements InstanceFilter {

    @Override
    public String id() {
        return "Composite";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private final List<InstanceFilter> delegates;

    public CompositeInstanceFilter(List<InstanceFilter> delegates) {
        this.delegates = CollectionUtils.isEmpty(delegates) ? emptyList() :
                delegates.stream().sorted(Comparator.comparingInt(InstanceFilter::getOrder)).collect(Collectors.toList());
    }

    /**
     * 筛选服务实例
     *
     * @param instances 实例列表
     * @param request   请求数据
     * @return 筛选后要保留的实例列表
     */
    @Override
    public List<Instance> filter(List<Instance> instances, TalbRequest request) {
        if (CollectionUtils.isEmpty(delegates)) {
            return instances;
        }

        for (InstanceFilter filter : delegates) {
            instances = filter.filter(instances, request);
        }
        return instances;
    }

}
