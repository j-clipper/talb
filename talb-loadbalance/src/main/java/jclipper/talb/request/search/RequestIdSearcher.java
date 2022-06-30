package jclipper.talb.request.search;

import jclipper.talb.base.TalbRequest;

/**
 * 从{@link TalbRequest}中搜索 requestId
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:55.
 */
public interface RequestIdSearcher extends RequestContentSearcher<String> {

    /**
     * 搜索并传递
     *
     * @param request
     * @return
     */
    @Override
    default String searchAndTransmit(TalbRequest request) {

        String requestId = search(request);
        if (requestId != null) {
            //如果找到requestId后，将其缓存到TalbRequest中，用于透传
            request.putAttribute(TalbRequest.REQUEST_ID_KEY, requestId);
        }
        return requestId;
    }
}
