package jclipper.talb.request.search;

import jclipper.talb.base.Instance;

/**
 * 查找服务实例的版本
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/3/3 16:47.
 */
public interface InstanceVersionSearcher {
    /**
     * 查找查找服务实例的版本
     *
     * @param instance Instance
     * @return 服务实例的版本
     */
    String search(Instance instance);
}
