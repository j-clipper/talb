package com.wf2311.talb.loadbalance.filter;

import com.wf2311.talb.base.Instance;
import com.wf2311.talb.base.InstanceFilter;
import com.wf2311.talb.base.Ordered;
import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.factory.RequestDirectIpInstanceFilterConfigProvider;
import com.wf2311.talb.request.search.RequestInstanceDirectIpAddressSearcher;
import com.wf2311.talb.request.search.impl.DefaultRequestInstanceDirectIpAddressSearcher;
import com.wf2311.talb.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 11:45.
 */
@Slf4j
public class RequestDirectIpInstanceFilter implements InstanceFilter {
    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE;

    private final RequestDirectIpInstanceFilterConfigProvider configProvider;

    private final RequestInstanceDirectIpAddressSearcher searcher;

    public RequestDirectIpInstanceFilter(RequestDirectIpInstanceFilterConfigProvider configProvider, RequestInstanceDirectIpAddressSearcher searcher) {
        this.configProvider = configProvider;
        this.searcher = searcher;
    }

    public RequestDirectIpInstanceFilter(RequestDirectIpInstanceFilterConfigProvider properties) {
        this(properties, new DefaultRequestInstanceDirectIpAddressSearcher());
    }

    @Override
    public String id() {
        return "RequestDirectIp";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public List<Instance> filter(List<Instance> instances, TalbRequest request) {
        return doFilter(instances, request);
    }

    @Override
    public List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request) {
        if (configProvider == null || searcher == null || request == null || !configProvider.isAllowDirectIp()) {
            return instances;
        }
        //搜索条件置于前面是为了在instances.size()=0时将request信息进行查找传递
        String ipAddress = searcher.searchAndTransmit(request);
        if (ipAddress == null) {
            return instances;
        }
        if (instances == null || instances.size() == 1) {
            return instances;
        }
        List<Instance> matchList = instances.stream().filter(instance -> ipAddress.equals(instance.getHost())).collect(Collectors.toList());
        if (matchList.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("not found any host={} in serviceId={} instances", ipAddress, instances.get(0).getServiceId());
            }
            return instances;
        }
        if (log.isDebugEnabled()) {
            log.debug("found  host={} instances :{}", ipAddress, JsonUtils.toJsonString(matchList));
        }

        markFiltered(request);

        return matchList;
    }
}
