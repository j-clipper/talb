package com.wf2311.talb.factory.base;

import com.wf2311.talb.factory.TalbRequestAttributeTransferConfigProvider;
import com.wf2311.talb.factory.internal.DefaultTalbRequestAttributeTransferConfigProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 19:51.
 */
public class TalbObjectFactory {
    private static final Map<Class<?>, List<Object>> OBJECT_MAP = new HashMap<>();

    /**
     * 获取指定类型的对象，如果不存在则返回null
     *
     * @param clazz 类型
     * @param <T>   对象类型
     * @return null or object
     */
    public static <T> T getObject(Class<T> clazz) {
        List<Object> objects = OBJECT_MAP.get(clazz);
        if (objects == null || objects.size() == 0) {
            return null;
        }
        //FIXME
        return (T) objects.get(objects.size() - 1);
    }

    /**
     * 获取指定类型的对象，如果不存在则抛出RuntimeException异常
     *
     * @param clazz 类型
     * @param <T>   对象类型
     * @return object
     * @throw RuntimeException
     */
    public static <T> T getDefaultObject(Class<T> clazz) {
        T object = getObject(clazz);
        if (object == null) {
            throw new RuntimeException("not found class [" + clazz.getName() + "] instance");
        }
        return object;
    }

    public static synchronized <T> void setDefaultObject(Class<T> clazz, T object) {
        List<Object> objects = OBJECT_MAP.computeIfAbsent(clazz, k -> new ArrayList<>());
        objects.add(object);
    }

    static {
        setDefaultObject(TalbRequestAttributeTransferConfigProvider.class, new DefaultTalbRequestAttributeTransferConfigProvider());
    }
}
