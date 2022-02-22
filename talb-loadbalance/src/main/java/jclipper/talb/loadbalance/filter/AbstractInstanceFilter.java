package jclipper.talb.loadbalance.filter;

import jclipper.talb.base.Instance;
import jclipper.talb.base.InstanceFilter;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.base.TalbRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract InstanceFilter
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/2/18 18:33.
 */
public abstract class AbstractInstanceFilter implements InstanceFilter {
    /**
     * 筛选服务实例，当instances为空或容量为1或context为null时，不进行筛选
     *
     * @param instances 实例列表
     * @param request   请求数据
     * @return 筛选后要保留的实例列表
     */
    @Override
    public List<Instance> filter(List<Instance> instances, TalbRequest request) {
        return doFilter(instances, request);
    }

    /**
     * 执行服务实例筛选
     *
     * @param instances 实例列表
     * @param request   请求数据
     * @return 筛选后要保留的实例列表
     */
    protected abstract List<Instance> doFilter(@NotEmpty List<Instance> instances, @NotNull TalbRequest request);


    /**
     * 标记instance已被Filter被过滤，使用{@link TalbConstants#INSTANCE_FILTER_MARKED_KEY}作为key, 以List为值保存在{@link TalbRequest#getAttributes()}中
     *
     * @param request 请求数据
     */
    protected void markFiltered(TalbRequest request) {
        List<String> markedList = (List<String>) request.getAttribute(TalbConstants.INSTANCE_FILTER_MARKED_KEY);
        if (markedList == null) {
            markedList = new ArrayList<>();
            request.getAttributes().put(TalbConstants.INSTANCE_FILTER_MARKED_KEY, markedList);
        }
        markedList.add(id());
    }
}
