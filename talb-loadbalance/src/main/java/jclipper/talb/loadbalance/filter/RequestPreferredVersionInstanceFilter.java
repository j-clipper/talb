package jclipper.talb.loadbalance.filter;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.RequestPreferredVersionConfigProvider;
import jclipper.talb.request.search.RequestPreferredVersionSearcher;
import jclipper.talb.request.search.impl.DefaultRequestPreferredVersionSearcher;
import jclipper.talb.utils.JsonUtils;
import jclipper.talb.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/2/17 14:22.
 */
@Slf4j
public class RequestPreferredVersionInstanceFilter extends AbstractInstanceFilter implements InstanceFilter {

    public static final int ORDER = RequestPreferredIpInstanceFilter.ORDER + 20;

    private final RequestPreferredVersionConfigProvider configProvider;

    private final RequestPreferredVersionSearcher searcher;

    public RequestPreferredVersionInstanceFilter(RequestPreferredVersionConfigProvider configProvider, RequestPreferredVersionSearcher searcher) {
        this.configProvider = configProvider;
        this.searcher = searcher;
    }

    public RequestPreferredVersionInstanceFilter(RequestPreferredVersionConfigProvider configProvider) {
        this(configProvider, new DefaultRequestPreferredVersionSearcher());
    }

    @Override
    public String id() {
        return "RequestPreferredVersionInstanceFilter";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request) {
        if (configProvider == null || searcher == null || request == null || !configProvider.isAllowPreferredVersion()) {
            return instances;
        }
        //搜索条件置于前面是为了在instances.size()=0时将request信息进行查找传递
        Set<String> preferredVersions = searcher.searchAndTransmit(request);
        if (preferredVersions == null || preferredVersions.isEmpty()) {
            return instances;
        }
        if (instances == null || instances.size() == 1) {
            return instances;
        }
        List<Instance> matchList = instances.stream().filter(i -> isMatchAnyVersion(i, preferredVersions)).collect(Collectors.toList());

        if (matchList.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("not found any version={} in preferredVersions={} instances", JsonUtils.toJsonString(preferredVersions), instances.get(0).getServiceId());
            }
            return instances;
        }
        if (log.isDebugEnabled()) {
            log.warn("found  preferredVersions={} instances :{}", JsonUtils.toJsonString(preferredVersions), JSON.toJSONString(matchList));
        }

        markFiltered(request);

        return matchList;
    }

    /**
     * 判断服务实例的ip是不是在指定的网段里面，匹配规则：正则匹配或前缀匹配
     *
     * @param instance          实例
     * @param preferredVersions 首选版本号
     * @return true/false
     */
    public static boolean isMatchAnyVersion(Instance instance, Set<String> preferredVersions) {
        return preferredVersions.stream().anyMatch(p -> isVersionMatch(instance, p));
    }

    /**
     * 判断服务实例的ip是不是属于指定的网段，匹配规则：正则匹配或前缀匹配
     *
     * @param instance         实例
     * @param preferredVersion 首选网络
     * @return true/false
     */
    public static boolean isVersionMatch(Instance instance, String preferredVersion) {
        if (Strings.isNullOrEmpty(preferredVersion)) {
            return true;
        }
        Map<String, String> metadata = instance.getMetadata();
        if (metadata == null) {
            return false;
        }
        String version = metadata.get(TalbConstants.INSTANCE_VERSION_KEY);
        if (Strings.isNullOrEmpty(version)) {
            return false;
        }
        if (preferredVersion.endsWith("*")) {
            String prefix = preferredVersion.substring(0, preferredVersion.length() - 1);
            return version.startsWith(prefix);
        }
        return version.equals(preferredVersion);
    }
}
