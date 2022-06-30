package jclipper.talb.request.search;

import jclipper.talb.base.TalbRequest;

/**
 * 从{@link TalbRequest}中搜索内容
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:39.
 */
public interface RequestContentSearcher<C> {
    /**
     * 内容搜索
     *
     * @param request
     * @return
     */
    C search(TalbRequest request);

    /**
     * 搜索并传递
     *
     * @param request
     * @return
     */
    default C searchAndTransmit(TalbRequest request) {
        throw new UnsupportedOperationException();
    }


}
