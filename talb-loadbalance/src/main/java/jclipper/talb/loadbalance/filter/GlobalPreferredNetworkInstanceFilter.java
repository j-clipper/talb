package jclipper.talb.loadbalance.filter;

import jclipper.common.utils.CollectionUtils;
import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.GlobalPreferredNetworkConfigProvider;
import jclipper.talb.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实例过滤器：全局的网段过滤，应用场景：例如在测试环境中对网段进行限定或过滤，防止开发者的注册的实例被调用
 * 对服务实例的ip按照 GlobalPreferredNetworkConfigProvider 中的allowNetworks、disableNetworks进行过滤
 * <p>该过滤器是否启用由 {@link GlobalPreferredNetworkConfigProvider#isEnabled()}进行控制，true表示进行过滤，false表示不进行过滤 </p>
 * <p>
 * 实例过滤是按照配置的服务网段进行过滤的，分为白名单和黑名单，顺序如下：
 * <li>1. 白名单：由{@link GlobalPreferredNetworkConfigProvider#getAllowNetworks()}提供，保留白名单中匹配实例，如果都不匹配，则保留所有实例</li>
 * <li>2. 黑名单：由{@link GlobalPreferredNetworkConfigProvider#getDisableNetworks()}提供，移除黑名单中匹配的实例，如果过滤后保留的实例列表为空，则回滚过滤保留上一部中所有的实例</li>
 * </p>
 * <p>
 * 例如存在如下几个服务实例，IP分别为
 * <li>192.168.3.10</li>
 * <li>192.168.3.11</li>
 * <li>192.168.4.10</li>
 * <li>192.168.5.10</li>
 * </p>
 * <p>
 * 过滤顺序：
 * <p>1. 白名单列表：
 * <li>192.168.3</li>
 * <li>192.168.4</li>
 * 保留匹配实例
 * <li>192.168.3.10</li>
 * <li>192.168.3.11</li>
 * <li>192.168.4.10</li>
 * </p>
 * <p>
 * 2. 黑名单列表：
 * <li>192.168.90</li>
 * 无匹配，保留所有实例
 * <li>192.168.3.10</li>
 * <li>192.168.3.11</li>
 * <li>192.168.4.10</li>
 * </p>
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 10:37.
 */
@Slf4j
public class GlobalPreferredNetworkInstanceFilter extends AbstractInstanceFilter implements InstanceFilter {

    public static final int ORDER = RequestPreferredNetworkInstanceFilter.ORDER + 10;

    private final GlobalPreferredNetworkConfigProvider configProvider;

    public GlobalPreferredNetworkInstanceFilter(GlobalPreferredNetworkConfigProvider properties) {
        this.configProvider = properties;
    }

    @Override
    public String id() {
        return "GlobalPreferredNetwork";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request) {
        //如果未启用，则不进行过滤
        if (configProvider == null || !configProvider.isEnabled()) {
            return instances;
        }

        Set<String> allowNetworks = configProvider.getAllowNetworks();
        List<Instance> filteredInstances = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allowNetworks)) {
            List<Instance> matchList = instances.stream().filter(i -> RequestUtils.isMatchAnyNetwork(i, allowNetworks)).collect(Collectors.toList());
            if (matchList.isEmpty()) {
                if (log.isWarnEnabled()) {
                    log.warn(" filtered by allowNetworks={} not any match instances , ignore condition with allowNetworks ", allowNetworks);
                }
                filteredInstances = instances;
            } else {
                filteredInstances = matchList;
            }
        }

        Set<String> disableNetworks = configProvider.getDisableNetworks();
        if (!CollectionUtils.isEmpty(disableNetworks)) {
            List<Instance> matchList = filteredInstances.stream().filter(i -> RequestUtils.isMatchAnyNetwork(i, allowNetworks)).collect(Collectors.toList());
            if (matchList.isEmpty()) {
                if (log.isWarnEnabled()) {
                    log.warn(" filtered by disableNetworks={} not any match instances , ignore condition with disableNetworks ", allowNetworks);
                }
            } else {
                filteredInstances = matchList;
            }
        }
        markFiltered(request);

        return filteredInstances;
    }
}
