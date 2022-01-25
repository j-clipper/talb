package com.wf2311.talb.request.search.impl;

import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.request.search.RequestInstancePreferredNetworksSearcher;
import com.wf2311.talb.utils.RequestUtils;

import java.util.Set;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:49.
 */
public class DefaultRequestInstancePreferredNetworksSearcher implements RequestInstancePreferredNetworksSearcher {

    @Override
    public Set<String> search(TalbRequest request) {

        Set<String> preferredNetworks = RequestUtils.findPreferredNetworks(request);
        if (preferredNetworks != null && preferredNetworks.size() > 0) {
            //如果找到preferredNetworks后，将其缓存到TalbRequest中，用于透传
            request.putAttribute(TalbRequest.PREFERRED_NETWORK_KEY, preferredNetworks);
        }
        return preferredNetworks;
    }

}
