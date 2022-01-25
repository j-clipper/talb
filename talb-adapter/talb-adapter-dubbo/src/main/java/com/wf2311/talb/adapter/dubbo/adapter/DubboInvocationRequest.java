package com.wf2311.talb.adapter.dubbo.adapter;

import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.request.AbstractRequest;
import org.apache.dubbo.rpc.Invocation;

import java.net.URISyntaxException;

import static java.util.Collections.emptyMap;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/18 10:12.
 */
public class DubboInvocationRequest extends AbstractRequest implements TalbRequest {

    private final Invocation invocation;

    public static TalbRequest adapter(Invocation data) {
        if (data == null) {
            return null;
        }

        try {
            return new DubboInvocationRequest(data);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private DubboInvocationRequest(Invocation invocation) throws URISyntaxException {
        this.invocation = invocation;

        this.uri = null;
        this.url = invocation.getInvoker().getUrl().toString();
        this.queryParams = invocation.getInvoker().getUrl().getParameters();

        this.serviceId = invocation.getServiceName();
        this.headers = emptyMap();
        this.cookies = emptyMap();
        this.attributes = invocation.getObjectAttachments();
    }
}
