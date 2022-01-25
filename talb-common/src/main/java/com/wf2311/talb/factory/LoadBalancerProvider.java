package com.wf2311.talb.factory;

import com.wf2311.talb.base.LoadBalancer;
import com.wf2311.talb.factory.base.TalbObjectProvider;

/**
 * LoadBalancer 提供者
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 19:25.
 */
@FunctionalInterface
public interface LoadBalancerProvider extends TalbObjectProvider<LoadBalancer> {

    /**
     * 获取LoadBalancer
     *
     * @return
     */
    @Override
    LoadBalancer get();

}
