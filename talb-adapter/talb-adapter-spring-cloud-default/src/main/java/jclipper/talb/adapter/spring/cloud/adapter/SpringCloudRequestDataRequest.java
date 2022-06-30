package jclipper.talb.adapter.spring.cloud.adapter;

import jclipper.talb.base.TalbRequest;
import jclipper.talb.request.AbstractRequest;
import lombok.Data;
import org.springframework.cloud.client.loadbalancer.RequestData;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

import static java.util.Collections.emptyMap;

/**
 * org.springframework.cloud.client.loadbalancer.RequestData适配TalbRequest
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 11:35.
 */
@Data
public class SpringCloudRequestDataRequest extends AbstractRequest implements TalbRequest {

    private RequestData data;

    public static TalbRequest adapter(String serviceId, RequestData data) {
        if (data == null) {
            return null;
        }
        return new SpringCloudRequestDataRequest(serviceId, data);
    }

    private SpringCloudRequestDataRequest(@NotNull String serviceId, @NotNull RequestData data) {
        if (data == null) {
            throw new NullPointerException("RequestData is Null");
        }
        this.data = data;
        this.uri = data.getUrl();
        this.url = data.getUrl().toString();
        this.serviceId = serviceId;
        this.headers = emptyMap();
        this.cookies = emptyMap();
        this.attributes = data.getAttributes();
        if (data.getHeaders() != null && data.getHeaders().size() > 0) {
            this.headers = new HashMap<>(data.getHeaders().size());
            data.getHeaders().forEach((k, v) -> this.headers.put(k, v.get(0)));
        }
        if (data.getCookies() != null && data.getCookies().size() > 0) {
            this.cookies = new HashMap<>(data.getCookies().size());
            data.getCookies().forEach((k, v) -> this.cookies.put(k, v.get(0)));
        }
        initQueryParams();
    }
}
