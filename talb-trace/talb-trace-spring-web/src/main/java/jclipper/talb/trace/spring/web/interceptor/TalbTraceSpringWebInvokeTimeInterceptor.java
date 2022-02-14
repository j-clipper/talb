package jclipper.talb.trace.spring.web.interceptor;

import com.alibaba.ttl.TransmittableThreadLocal;
import jclipper.talb.context.TalbContext;
import jclipper.talb.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TLog web的调用时间统计拦截器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 11:03.
 */
@Slf4j
public class TalbTraceSpringWebInvokeTimeInterceptor extends AbstractTalbTraceHandlerMethodInterceptor {


    private final TransmittableThreadLocal<StopWatch> invokeTimeTL = new TransmittableThreadLocal<>();

    @Override
    public boolean preHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (TalbContext.enableInvokeTimePrint()) {
            String url = request.getRequestURI();

            String parameters = JsonUtils.toJsonString(request.getParameterMap());
            if (log.isDebugEnabled()) {
                log.debug("[TALB]开始请求URL[{}],参数为:{}", url, parameters);
            }

            StopWatch stopWatch = new StopWatch();
            invokeTimeTL.set(stopWatch);
            stopWatch.start();
        }
        return true;
    }

    @Override
    public void postHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletionByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (TalbContext.enableInvokeTimePrint()) {
            StopWatch stopWatch = invokeTimeTL.get();
            stopWatch.stop();
            if (log.isDebugEnabled()) {
                log.debug("[TALB]结束URL[{}]的调用,耗时为:{}毫秒", request.getRequestURI(), stopWatch.getTime());
            }
            invokeTimeTL.remove();
        }
    }
}
