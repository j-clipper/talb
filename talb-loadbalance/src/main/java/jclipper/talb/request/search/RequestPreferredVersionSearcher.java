package jclipper.talb.request.search;

import jclipper.talb.base.TalbRequest;

import java.util.Set;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/2/22 09:52.
 */
public interface RequestPreferredVersionSearcher extends RequestContentSearcher<Set<String>> {
    /**
     * 搜索并传递
     *
     * @param request
     * @return
     */
    @Override
    default Set<String> searchAndTransmit(TalbRequest request) {
        Set<String> preferredAddress = search(request);
        if (preferredAddress != null) {
            //如果找到preferredNetworks后，将其缓存到TalbRequest中，用于透传
            request.putAttribute(TalbRequest.PREFERRED_VERSION_KEY, preferredAddress);
        }
        return preferredAddress;
    }

}
