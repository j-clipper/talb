package jclipper.talb.loadbalance.filter;

import com.alibaba.fastjson.JSON;
import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.RequestPreferredNetworkInstanceFilterConfigProvider;
import jclipper.talb.request.search.RequestInstancePreferredNetworksSearcher;
import jclipper.talb.request.search.impl.DefaultRequestInstancePreferredNetworksSearcher;
import jclipper.talb.utils.JsonUtils;
import jclipper.talb.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 11:48.
 */
@Slf4j
public class RequestPreferredNetworkInstanceFilter implements InstanceFilter {

    public static final int ORDER = RequestDirectIpInstanceFilter.ORDER + 10;

    private final RequestPreferredNetworkInstanceFilterConfigProvider configProvider;

    private final RequestInstancePreferredNetworksSearcher searcher;

    public RequestPreferredNetworkInstanceFilter(RequestPreferredNetworkInstanceFilterConfigProvider configProvider, RequestInstancePreferredNetworksSearcher searcher) {
        this.configProvider = configProvider;
        this.searcher = searcher;
    }

    public RequestPreferredNetworkInstanceFilter(RequestPreferredNetworkInstanceFilterConfigProvider configProvider) {
        this(configProvider, new DefaultRequestInstancePreferredNetworksSearcher());
    }

    @Override
    public String id() {
        return "RequestPreferredNetwork";
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
        if (configProvider == null || searcher == null || request == null || !configProvider.isAllowPreferredNetworks()) {
            return instances;
        }
        //搜索条件置于前面是为了在instances.size()=0时将request信息进行查找传递
        Set<String> preferredNetworks = searcher.searchAndTransmit(request);
        if (preferredNetworks == null || preferredNetworks.isEmpty()) {
            return instances;
        }
        if (instances == null || instances.size() == 1) {
            return instances;
        }
        List<Instance> matchList = instances.stream().filter(i -> RequestUtils.isMatchAny(i, preferredNetworks)).collect(Collectors.toList());

        if (matchList.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("not found any host={} in preferredNetworks={} instances", JsonUtils.toJsonString(preferredNetworks), instances.get(0).getServiceId());
            }
            return instances;
        }
        if (log.isDebugEnabled()) {
            log.warn("found  preferredNetworks={} instances :{}", JsonUtils.toJsonString(preferredNetworks), JSON.toJSONString(matchList));
        }

        markFiltered(request);

        return matchList;
    }

}
