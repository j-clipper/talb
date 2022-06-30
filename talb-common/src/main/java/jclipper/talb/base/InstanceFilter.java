package jclipper.talb.base;

import java.util.List;

/**
 * 服务实例过滤器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:11.
 */
public interface InstanceFilter {

    /**
     * 过滤器的ID
     *
     * @return
     */
    String id();

    /**
     * 过滤器的执行顺序
     *
     * @return 执行顺序
     */
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * 筛选服务实例
     *
     * @param instances 实例列表
     * @param request   请求数据
     * @return 筛选后要保留的实例列表
     */
    List<Instance> filter(List<Instance> instances, TalbRequest request);

}
