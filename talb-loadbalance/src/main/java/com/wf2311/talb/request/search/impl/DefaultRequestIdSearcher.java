package com.wf2311.talb.request.search.impl;

import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.request.search.RequestIdSearcher;
import com.wf2311.talb.utils.RequestUtils;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:44.
 */
public class DefaultRequestIdSearcher implements RequestIdSearcher {

    @Override
    public String search(TalbRequest request) {

        String requestId = RequestUtils.findRequestId(request);
        if (requestId != null) {
            //如果找到requestId后，将其缓存到TalbRequest中，用于透传
            request.putAttribute(TalbRequest.REQUEST_ID_KEY, requestId);
        }
        return requestId;
    }

}
