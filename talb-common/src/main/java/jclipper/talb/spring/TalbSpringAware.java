package jclipper.talb.spring;

import jclipper.talb.base.TalbConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * Spring的上下文处理器
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 11:03.
 */
public class TalbSpringAware implements ApplicationContextAware {


    private static ApplicationContext applicationContext = null;

    private static Environment environment = null;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) applicationContext.getBean(clazz);
    }

    public static <T> T registerBean(Class<T> c) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(c.getName());
        beanFactory.registerBeanDefinition(c.getName(), beanDefinition);
        return getBean(c.getName());
    }

    public static String getProperty(String key) {
        if (environment == null) {
            environment = getBean(Environment.class);
        }
        return environment.getProperty(key);
    }

    public static String getAppName(){
        return getProperty(TalbConstants.SPRING_APP_NAME_KEY);
    }
}
