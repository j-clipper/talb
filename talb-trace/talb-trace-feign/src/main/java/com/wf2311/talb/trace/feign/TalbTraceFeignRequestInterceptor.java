package com.wf2311.talb.trace.feign;

import com.wf2311.talb.context.TalbContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Map;

/**
 * feign的拦截器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/24 15:43.
 */
public class TalbTraceFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Map<String, String> traceAttributes = TalbContext.getTraceMapForNext();
        Map<String, String> requestAttributes = TalbContext.getRequestAttributesForNext();

        if (traceAttributes.size() > 0) {
            traceAttributes.forEach(template::header);
        }
        if (requestAttributes != null && requestAttributes.size() > 0) {
            requestAttributes.forEach(template::header);
        }
    }
}
