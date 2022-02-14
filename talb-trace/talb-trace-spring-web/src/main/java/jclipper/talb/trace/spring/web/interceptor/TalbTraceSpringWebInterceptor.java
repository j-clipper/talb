package jclipper.talb.trace.spring.web.interceptor;

import jclipper.talb.trace.spring.web.TalbSpringWebHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web controller的拦截器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 1.1.5
 */
public class TalbTraceSpringWebInterceptor extends AbstractTalbTraceHandlerMethodInterceptor {

    @Override
    public boolean preHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TalbSpringWebHandler.loadInstance().prevHandle(request);
        return true;
    }

    @Override
    public void postHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        TalbSpringWebHandler.loadInstance().postHandle(request, response);
    }

    @Override
    public void afterCompletionByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TalbSpringWebHandler.loadInstance().afterCompletion();
    }
}
