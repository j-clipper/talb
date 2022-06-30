package jclipper.talb.loadbalance.filter;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.RequestPreferredVersionConfigProvider;
import jclipper.talb.request.search.InstanceVersionSearcher;
import jclipper.talb.request.search.RequestPreferredVersionSearcher;
import jclipper.talb.request.search.impl.DefaultInstanceVersionSearcher;
import jclipper.talb.request.search.impl.DefaultRequestPreferredVersionSearcher;
import jclipper.talb.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实例过滤器：根据请求端偏好的服务实例版本号对服务实例进行过滤
 *
 * <p>
 * 该过滤器是否启用由 {@link RequestPreferredVersionConfigProvider#isAllowPreferredVersion()} 进行控制，true表示进行过滤，false表示不进行过滤。
 * <p>
 * 使用 {@link InstanceVersionSearcher#search(Instance)}对服务实例中携带的版本号进行查找
 * <p>
 * 进行匹配时，如果存在匹配实例，则返回匹配实例；如果没有匹配条件(指定的版本号)或者无匹配实例，则不进行过滤，返回所有实例
 * <strong>匹配是支持前缀匹配的，即如果偏好的版本号为<code>a.b.*</code>，则以<code>a.b.</code>开头的版本号实例都会被匹配到</strong>
 * <p>
 * 例如存在如下几个服务实例，其版本号分别为
 * <li>1.1.2</li>
 * <li>1.1.3</li>
 * <li>1.2.1</li>
 * <li>&lt;nil&gt;</li>
 * </p>
 * <p>
 * 情况一：如果根据 {@link RequestPreferredVersionSearcher#search(TalbRequest)} 找到的请求者偏好的服务实例版本号为
 * <li>1.1.*</li>
 * <li>1.2.1</li>
 * 则version=1.1.2、version=1.1.3、version=1.2.1的三个实例会被选中保留
 * </p>
 * <p>
 * 情况二：如果根据 {@link RequestPreferredVersionSearcher#search(TalbRequest)} 找到的请求者偏好的实例版本号为
 * <li>1.3.*</li>
 * 没有匹配的实例，则不进行过滤，返回所有的实例
 * </p>
 * <p>
 * 情况三：如果根据 {@link RequestPreferredVersionSearcher#search(TalbRequest)} 没有找到的携带的请求这偏好的实例版本号信息
 * 则也不进行过滤，返回所有的实例
 * </p>
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/2/17 14:22.
 */
@Slf4j
public class RequestPreferredVersionInstanceFilter extends AbstractInstanceFilter implements InstanceFilter {

    public static final int ORDER = RequestPreferredIpInstanceFilter.ORDER + 20;

    private final RequestPreferredVersionConfigProvider configProvider;

    private final RequestPreferredVersionSearcher searcher;

    private final InstanceVersionSearcher instanceVersionSearcher;
    /**
     * 服务实例版本号通配符
     */
    private static final String VERSION_END_WILDCARD = "*";

    public RequestPreferredVersionInstanceFilter(RequestPreferredVersionConfigProvider configProvider, RequestPreferredVersionSearcher searcher, InstanceVersionSearcher instanceVersionSearcher) {
        this.configProvider = configProvider;
        this.searcher = searcher;
        this.instanceVersionSearcher = instanceVersionSearcher;
    }

    public RequestPreferredVersionInstanceFilter(RequestPreferredVersionConfigProvider configProvider) {
        this(configProvider, new DefaultRequestPreferredVersionSearcher(), new DefaultInstanceVersionSearcher());
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
     * 判断服务实例的版本号是不是在指定的版本里面，匹配规则：正则匹配或前缀匹配
     *
     * @param instance          实例
     * @param preferredVersions 首选版本号
     * @return true/false
     */
    private boolean isMatchAnyVersion(Instance instance, Set<String> preferredVersions) {
        return preferredVersions.stream().anyMatch(p -> isVersionMatch(instance, p));
    }

    /**
     * 判断服务实例的ip是不是属于指定的版本，匹配规则：正则匹配或前缀匹配
     *
     * @param instance         实例
     * @param preferredVersion 首选网络
     * @return true/false
     */
    private boolean isVersionMatch(Instance instance, String preferredVersion) {
        if (Strings.isNullOrEmpty(preferredVersion)) {
            return true;
        }
        String version = instanceVersionSearcher.search(instance);
        if (Strings.isNullOrEmpty(version)) {
            return false;
        }
        if (preferredVersion.endsWith(VERSION_END_WILDCARD)) {
            String prefix = preferredVersion.substring(0, preferredVersion.length() - 1);
            return version.startsWith(prefix);
        }
        return version.equals(preferredVersion);
    }
}
