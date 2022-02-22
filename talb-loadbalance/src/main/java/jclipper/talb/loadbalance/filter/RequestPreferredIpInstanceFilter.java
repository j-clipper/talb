package jclipper.talb.loadbalance.filter;

import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.Ordered;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.RequestPreferredIpConfigProvider;
import jclipper.talb.request.search.RequestPreferredIpAddressSearcher;
import jclipper.talb.request.search.impl.DefaultRequestPreferredIpAddressSearcher;
import jclipper.talb.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实例过滤器：根据请求端偏好的服务实例Ip对服务实例进行选择过滤
 *
 * <p>
 * 该过滤器是否启用由 {@link RequestPreferredIpConfigProvider#isAllowPreferredIp()} 进行控制，true表示进行过滤，false表示不进行过滤
 * 进行匹配时，如果存在匹配实例，则返回匹配实例；如果没有匹配条件(指定的Ip)或者无匹配实例，则不进行过滤，返回所有实例
 * </p>
 * <p>
 * 例如存在如下几个服务实例，IP分别为
 * <li>192.168.3.10</li>
 * <li>192.168.3.11</li>
 * <li>192.168.4.10</li>
 * <li>192.168.5.10</li>
 * </p>
 * <p>
 * 情况一：如果根据 {@link RequestPreferredIpAddressSearcher#search(TalbRequest)} 找到的请求这偏好的IP为
 * <li>192.168.3.10</li>
 * <li>192.168.3.11</li>
 * 则ip=192.168.3.10、ip=192.168.3.11的两个实例会被选中保留
 * </p>
 * <p>
 * 情况二：如果根据 {@link RequestPreferredIpAddressSearcher#search(TalbRequest)} 找到的请求这偏好的IP为
 * <li>192.168.6.10</li>
 * 没有匹配的实例，则不进行过滤，返回所有的实例
 * </p>
 * <p>
 * 情况三：如果根据 {@link RequestPreferredIpAddressSearcher#search(TalbRequest)} 没有找到的携带的请求这偏好的IP信息
 * 则也不进行过滤，返回所有的实例
 * </p>
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 11:45.
 */
@Slf4j
public class RequestPreferredIpInstanceFilter extends AbstractInstanceFilter implements InstanceFilter {
    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE;

    private final RequestPreferredIpConfigProvider configProvider;

    private final RequestPreferredIpAddressSearcher searcher;

    public RequestPreferredIpInstanceFilter(RequestPreferredIpConfigProvider configProvider, RequestPreferredIpAddressSearcher searcher) {
        this.configProvider = configProvider;
        this.searcher = searcher;
    }

    public RequestPreferredIpInstanceFilter(RequestPreferredIpConfigProvider properties) {
        this(properties, new DefaultRequestPreferredIpAddressSearcher());
    }

    @Override
    public String id() {
        return "RequestPreferredIp";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request) {
        if (configProvider == null || searcher == null || request == null || !configProvider.isAllowPreferredIp()) {
            return instances;
        }
        //搜索条件置于前面是为了在instances.size()=0时将request信息进行查找传递
        Set<String> ipAddresses = searcher.searchAndTransmit(request);
        if (ipAddresses == null || ipAddresses.size() == 0) {
            return instances;
        }
        if (instances == null || instances.size() == 1) {
            return instances;
        }
        List<Instance> matchList = instances.stream().filter(instance -> ipAddresses.contains(instance.getHost())).collect(Collectors.toList());
        if (matchList.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("not found any host={} in serviceId={} instances", JsonUtils.toJsonString(ipAddresses), instances.get(0).getServiceId());
            }
            return instances;
        }
        if (log.isDebugEnabled()) {
            log.debug("found  host={} instances :{}", JsonUtils.toJsonString(ipAddresses), JsonUtils.toJsonString(matchList));
        }

        markFiltered(request);

        return matchList;
    }
}
