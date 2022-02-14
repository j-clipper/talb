package jclipper.talb.loadbalance.filter;

import jclipper.common.utils.CollectionUtils;
import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.GlobalPreferredNetworkInstanceFilterConfigProvider;
import jclipper.talb.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实例过滤器：全局的网段过滤，对服务实例的ip按照 GlobalPreferredNetworkConfigProvider 中的allowNetworks、disableNetworks进行过滤
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 10:37.
 */
@Slf4j
public class GlobalPreferredNetworkInstanceFilter implements InstanceFilter {

    public static final int ORDER = RequestPreferredNetworkInstanceFilter.ORDER + 10;

    private final GlobalPreferredNetworkInstanceFilterConfigProvider configProvider;

    public GlobalPreferredNetworkInstanceFilter(GlobalPreferredNetworkInstanceFilterConfigProvider properties) {
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
    public List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request) {
        //如果不是测试环境，则不进行过滤
        if (configProvider == null || !configProvider.isEnabled()) {
            return instances;
        }

        Set<String> allowNetworks = configProvider.getAllowNetworks();
        List<Instance> filteredInstances = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allowNetworks)) {
            List<Instance> matchList = instances.stream().filter(i -> RequestUtils.isMatchAny(i, allowNetworks)).collect(Collectors.toList());
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
            List<Instance> matchList = filteredInstances.stream().filter(i -> RequestUtils.isMatchAny(i, allowNetworks)).collect(Collectors.toList());
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
