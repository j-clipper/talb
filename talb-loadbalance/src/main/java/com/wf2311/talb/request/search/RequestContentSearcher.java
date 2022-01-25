package com.wf2311.talb.request.search;

import com.wf2311.talb.base.TalbRequest;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:39.
 */
public interface RequestContentSearcher<C> {
    /**
     * 内容搜索
     *
     * @param request
     * @return
     */
    C search(TalbRequest request);
}
