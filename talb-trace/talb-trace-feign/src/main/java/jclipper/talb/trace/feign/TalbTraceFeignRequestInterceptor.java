package jclipper.talb.trace.feign;

import jclipper.talb.context.TalbContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Map;

/**
 * 拦截器feign client中对 Talb Trace的设值传递
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
