package jclipper.talb.trace.spring.web;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import jclipper.talb.trace.spring.web.interceptor.TalbTraceSpringWebInterceptor;
import jclipper.talb.trace.spring.web.interceptor.TalbTraceSpringWebInvokeTimeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;

/**
 * Talb webconfig类
 * @author wf2311
 */
@Configuration
public class TalbWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration;
        interceptorRegistration = registry.addInterceptor(new TalbTraceSpringWebInterceptor());
        //这里是为了兼容springboot 1.5.X，1.5.x没有order这个方法
        try {
            Method method = ReflectUtil.getMethod(InterceptorRegistration.class, "order", Integer.class);
            if (ObjectUtil.isNotNull(method)) {
                method.invoke(interceptorRegistration, Ordered.HIGHEST_PRECEDENCE);
            }
        } catch (Exception e) {

        }
        interceptorRegistration = registry.addInterceptor(new TalbTraceSpringWebInvokeTimeInterceptor());
        //这里是为了兼容springboot 1.5.X，1.5.x没有order这个方法
        try {
            Method method = ReflectUtil.getMethod(InterceptorRegistration.class, "order", Integer.class);
            if (ObjectUtil.isNotNull(method)) {
                method.invoke(interceptorRegistration, Ordered.HIGHEST_PRECEDENCE);
            }
        } catch (Exception e) {

        }
    }
}
