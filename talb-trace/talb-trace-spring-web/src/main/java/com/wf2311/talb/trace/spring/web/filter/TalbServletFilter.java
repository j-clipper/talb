package com.wf2311.talb.trace.spring.web.filter;

import com.wf2311.talb.trace.spring.web.TalbSpringWebHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 支持servlet
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 1.3.5
 */
public class TalbServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            try {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                TalbSpringWebHandler.loadInstance().prevHandle(httpServletRequest);
                TalbSpringWebHandler.loadInstance().postHandle(httpServletRequest, (HttpServletResponse) response);

            } finally {
                TalbSpringWebHandler.loadInstance().afterCompletion();
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
