package jclipper.talb.base;

import java.util.List;

/**
 * 负载均衡器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 14:48.
 */
public interface LoadBalancer {

    /**
     * 根据负载均衡策略选择实例
     *
     * @param instances 实例列表
     * @param request   请求
     * @return
     */
    default Instance choose(List<Instance> instances, TalbRequest request) {
        if (request == null) {
            return null;
        }
        return doChoose(instances, request);
    }

    /**
     * 根据负载均衡策略选择实例
     *
     * @param instances 实例列表
     * @param request   请求
     * @return
     */
    Instance doChoose(List<Instance> instances, TalbRequest request);

}
