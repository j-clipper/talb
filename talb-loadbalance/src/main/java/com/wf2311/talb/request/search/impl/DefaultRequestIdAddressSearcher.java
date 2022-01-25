package com.wf2311.talb.request.search.impl;

import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.request.search.RequestIdAddressSearcher;
import com.wf2311.talb.utils.RequestUtils;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 21:05.
 */
public class DefaultRequestIdAddressSearcher implements RequestIdAddressSearcher {

    @Override
    public String search(TalbRequest request) {
        return RequestUtils.findDirectIpAddress(request);
    }

}
