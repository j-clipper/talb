package jclipper.talb.request.search.impl;

import jclipper.talb.base.TalbRequest;
import jclipper.talb.request.search.RequestPreferredVersionSearcher;
import jclipper.talb.utils.RequestUtils;

import java.util.Set;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/2/22 10:24.
 */
public class DefaultRequestPreferredVersionSearcher implements RequestPreferredVersionSearcher {
    @Override
    public Set<String> search(TalbRequest request) {
        return RequestUtils.findPreferredVersions(request);
    }
}
