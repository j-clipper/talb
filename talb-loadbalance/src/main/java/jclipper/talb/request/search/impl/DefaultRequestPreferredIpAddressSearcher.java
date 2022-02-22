package jclipper.talb.request.search.impl;

import jclipper.talb.base.TalbRequest;
import jclipper.talb.request.search.RequestPreferredIpAddressSearcher;
import jclipper.talb.utils.RequestUtils;

import java.util.Set;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:44.
 */
public class DefaultRequestPreferredIpAddressSearcher implements RequestPreferredIpAddressSearcher {

    @Override
    public Set<String> search(TalbRequest request) {
        return RequestUtils.findPreferredIpAddress(request);
    }
}
