package jclipper.talb.request.search.impl;

import jclipper.talb.base.TalbRequest;
import jclipper.talb.request.search.RequestIdSearcher;
import jclipper.talb.utils.RequestUtils;

/**
 * 默认的{@link RequestIdSearcher}实现类，调用{@link RequestUtils#findRequestId(TalbRequest)}搜索requestId
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:44.
 */
public class DefaultRequestIdSearcher implements RequestIdSearcher {
    @Override
    public String search(TalbRequest request) {
        return RequestUtils.findRequestId(request);
    }

}
