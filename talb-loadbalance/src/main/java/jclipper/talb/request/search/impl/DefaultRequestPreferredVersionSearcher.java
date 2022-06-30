package jclipper.talb.request.search.impl;

import jclipper.talb.base.TalbRequest;
import jclipper.talb.request.search.RequestPreferredVersionSearcher;
import jclipper.talb.utils.RequestUtils;

import java.util.Set;

/**
 * 默认的{@link RequestPreferredVersionSearcher}实现类，调用{@link RequestUtils#findPreferredVersions(TalbRequest)}搜索requestPreferredVersion
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/2/22 10:24.
 */
public class DefaultRequestPreferredVersionSearcher implements RequestPreferredVersionSearcher {
    @Override
    public Set<String> search(TalbRequest request) {
        return RequestUtils.findPreferredVersions(request);
    }
}
