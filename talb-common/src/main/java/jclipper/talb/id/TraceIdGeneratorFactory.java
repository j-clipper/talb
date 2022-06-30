package jclipper.talb.id;

import jclipper.talb.base.TalbIdGenerator;

/**
 * TraceId生成器工厂
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 17:46.
 */
public class TraceIdGeneratorFactory {

    /**
     * ID生成器
     */
    private static TalbIdGenerator idGenerator = new TalbDefaultIdGenerator();

    /**
     * 获取ID生成器
     */
    public static TalbIdGenerator get() {
        return idGenerator;
    }

    /**
     * 设置ID生成器
     *
     * @param idGenerator ID生成器
     */
    public static void setIdGenerator(TalbIdGenerator idGenerator) {
        TraceIdGeneratorFactory.idGenerator = idGenerator;
    }
}
