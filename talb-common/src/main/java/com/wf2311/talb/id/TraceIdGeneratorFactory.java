package com.wf2311.talb.id;

import com.wf2311.talb.base.TalbIdGenerator;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 17:46.
 */
public class TraceIdGeneratorFactory {

    private static TalbIdGenerator idGenerator = new TalbDefaultIdGenerator();

    public static TalbIdGenerator get() {
        return idGenerator;
    }

    public static void setIdGenerator(TalbIdGenerator idGenerator) {
        TraceIdGeneratorFactory.idGenerator = idGenerator;
    }
}
