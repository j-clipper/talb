package jclipper.talb.request.search;


import jclipper.talb.base.TalbRequest;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 20:45.
 */
public interface RequestInstanceDirectIpAddressSearcher extends RequestContentSearcher<String> {

    /**
     * 搜索并传递
     *
     * @param request
     * @return
     */
    @Override
    default String searchAndTransmit(TalbRequest request) {
        String directIpAddress = search(request);
        if (directIpAddress != null) {
            //如果找到preferredNetworks后，将其缓存到TalbRequest中，用于透传
            request.putAttribute(TalbRequest.DIRECT_IP_KEY, directIpAddress);
        }
        return directIpAddress;
    }
}
