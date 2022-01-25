package com.wf2311.talb.request;


import com.wf2311.talb.base.TalbRequest;
import lombok.Data;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 11:47.
 */
@Data
public abstract class AbstractRequest implements TalbRequest {

    protected String serviceId;

    protected String url;

    protected URI uri;

    protected Map<String, String> headers;

    protected Map<String, String> cookies;

    protected Map<String, String> queryParams;

    protected Map<String, Object> attributes;


    protected void initQueryParams() {
        this.queryParams = convertQueryParams();
    }

    protected Map<String, String> convertQueryParams() {
        if (this.getUri() == null) {
            return emptyMap();
        }
        String query = this.getUri().getQuery();
        if (query == null) {
            return null;
        }

        String[] array = query.split("&");
        Map<String, String> params = new HashMap<>(array.length);
        for (String p : array) {
            String[] split = p.split("=", 2);
            if (split.length == 2) {
                params.put(split[0], split[1]);
            }
        }
        return params;
    }
}
