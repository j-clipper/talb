package jclipper.talb.factory.base;

/**
 * 对象提供者
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 19:39.
 */
@FunctionalInterface
public interface TalbObjectProvider<T> {

    /**
     * 获取对象
     *
     * @return
     */
    T get();
}
