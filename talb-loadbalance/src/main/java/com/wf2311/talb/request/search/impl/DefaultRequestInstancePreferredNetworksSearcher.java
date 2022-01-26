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
        return RequestUtils.findPreferredNetworks(request);
    }
}
