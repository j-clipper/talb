package jclipper.talb.loadbalance.filter;

import com.alibaba.fastjson.JSON;
import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.RequestPreferredNetworkConfigProvider;
import jclipper.talb.request.search.RequestPreferredNetworksSearcher;
import jclipper.talb.request.search.impl.DefaultRequestPreferredNetworksSearcher;
import jclipper.talb.utils.JsonUtils;
import jclipper.talb.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实例过滤器：根据请求端偏好的服务实例网段对服务实例进行过滤
 *
 * <p>
 * 该过滤器是否启用由 {@link RequestPreferredNetworkConfigProvider#isAllowPreferredNetworks()}进行控制，true表示进行过滤，false表示不进行过滤
 * 进行匹配时，如果存在匹配实例，则返回匹配实例；如果没有匹配条件(指定的网段)或者无匹配实例，则不进行过滤，返回所有实例
 * <p>
 * 例如存在如下几个服务实例，IP分别为
 * <li>192.168.3.10</li>
 * <li>192.168.3.11</li>
 * <li>192.168.4.10</li>
 * <li>192.168.5.10</li>
 * </p>
 * <p>
 * 情况一：如果根据 {@link RequestPreferredNetworksSearcher#search(TalbRequest)} 找到的请求者偏好的网段为
 * <li>192.168.3</li>
 * <li>192.168.5</li>
 * 则ip=192.168.3.10、ip=192.168.3.11、ip=192.168.5.10的三个实例会被选中保留
 * </p>
 * <p>
 * 情况二：如果根据 {@link RequestPreferredNetworksSearcher#search(TalbRequest)} 找到的请求者偏好的网段为
 * <li>192.168.6</li>
 * 没有匹配的实例，则不进行过滤，返回所有的实例
 * </p>
 * <p>
 * 情况三：如果根据 {@link RequestPreferredNetworksSearcher#search(TalbRequest)} 没有找到的携带的请求这偏好的网段信息
 * 则也不进行过滤，返回所有的实例
 * </p>
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 11:48.
 */
@Slf4j
public class RequestPreferredNetworkInstanceFilter extends AbstractInstanceFilter implements InstanceFilter {

    public static final int ORDER = RequestPreferredIpInstanceFilter.ORDER + 10;

    private final RequestPreferredNetworkConfigProvider configProvider;

    private final RequestPreferredNetworksSearcher searcher;

    public RequestPreferredNetworkInstanceFilter(RequestPreferredNetworkConfigProvider configProvider, RequestPreferredNetworksSearcher searcher) {
        this.configProvider = configProvider;
        this.searcher = searcher;
    }

    public RequestPreferredNetworkInstanceFilter(RequestPreferredNetworkConfigProvider configProvider) {
        this(configProvider, new DefaultRequestPreferredNetworksSearcher());
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
    protected List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request) {
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
        List<Instance> matchList = instances.stream().filter(i -> RequestUtils.isMatchAnyNetwork(i, preferredNetworks)).collect(Collectors.toList());

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
