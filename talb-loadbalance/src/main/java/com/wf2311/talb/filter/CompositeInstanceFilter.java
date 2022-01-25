package com.wf2311.talb.filter;

import com.wf2311.common.utils.CollectionUtils;
import com.wf2311.talb.base.Instance;
import com.wf2311.talb.base.InstanceFilter;
import com.wf2311.talb.base.Ordered;
import com.wf2311.talb.base.TalbRequest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
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

    @Override
    public List<Instance> doFilter(List<Instance> instances, TalbRequest request) {

        if (CollectionUtils.isEmpty(delegates)) {
            return instances;
        }

        for (InstanceFilter filter : delegates) {
            instances = filter.filter(instances, request);
        }
        return instances;
    }

}
