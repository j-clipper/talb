package com.wf2311.talb.request.search.impl;

import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.request.search.RequestInstanceDirectIpAddressSearcher;
import com.wf2311.talb.utils.RequestUtils;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:44.
 */
public class DefaultRequestInstanceDirectIpAddressSearcher implements RequestInstanceDirectIpAddressSearcher {

    @Override
    public String search(TalbRequest request) {
        String directIpAddress = RequestUtils.findDirectIpAddress(request);

        if (directIpAddress != null) {
            //如果找到preferredNetworks后，将其缓存到TalbRequest中，用于透传
            request.putAttribute(TalbRequest.DIRECT_IP_KEY, directIpAddress);
        }
        return directIpAddress;
    }

}
