package jclipper.talb.base;

import java.net.URI;
import java.util.Map;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 21:40.
 */
public class TalbAttributesRequest implements TalbRequest {

    protected Map<String, Object> attributes;

    protected String serviceId;

    public TalbAttributesRequest(Map<String, Object> attributes) {
        this(attributes, null);
    }

    public TalbAttributesRequest(Map<String, Object> attributes, String serviceId) {
        this.attributes = attributes;
        this.serviceId = serviceId;
    }

    @Override
    public String getServiceId() {
        return null;
    }

    @Override
    public String getUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getUri() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getQueryParams() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getHeaders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getCookies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
