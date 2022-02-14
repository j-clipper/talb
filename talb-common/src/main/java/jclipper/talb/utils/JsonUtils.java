package jclipper.talb.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:58.
 */
public class JsonUtils {
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> List<T> parseArray(String text,Class<T> clazz){
        return JSON.parseArray(text, clazz);
    }
}
