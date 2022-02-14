package jclipper.talb.base;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务实例过滤器
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:11.
 */
public interface InstanceFilter {

    /**
     * ID
     *
     * @return
     */
    String id();

    /**
     * 序号
     *
     * @return
     */
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * 筛选服务实例，当instances为空或容量为1或context为null时，不进行筛选
     *
     * @param instances 实例列表
     * @param request   请求数据
     * @return 筛选后要保留的实例列表
     */
    default List<Instance> filter(List<Instance> instances, TalbRequest request) {
        if (instances == null || instances.size() == 1 || request == null) {
            return instances;
        }
        return doFilter(instances, request);
    }

    /**
     * 筛选服务实例，当instances为空或容量为1或context为null时，不进行筛选
     *
     * @param instances 实例列表
     * @param request   请求数据
     * @return 筛选后要保留的实例列表
     */
    List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request);

    /**
     * 标记instance已被Filter被过滤
     *
     * @param request 请求数据
     */
    default void markFiltered(TalbRequest request) {
        List<String> markedList = (List<String>)request.getAttribute(TalbConstants.INSTANCE_FILTER_MARKED_KEY);
        if (markedList == null) {
            markedList= new ArrayList<>();
            request.getAttributes().put(TalbConstants.INSTANCE_FILTER_MARKED_KEY, markedList);
        }
        markedList.add(id());
    }
}
