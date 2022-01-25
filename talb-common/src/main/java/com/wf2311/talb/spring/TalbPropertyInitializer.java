package com.wf2311.talb.spring;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wf2311.talb.base.TalbIdGenerator;
import com.wf2311.talb.context.TalbContext;
import com.wf2311.talb.id.TraceIdGeneratorFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

/**
 * TLog参数初始化类，适用于springboot和spring
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 11:03.
 */
@Getter
@Setter
public class TalbPropertyInitializer implements InitializingBean {

    private Boolean enableInvokeTimePrint;

    private String idGenerator;

    private Boolean mdcEnable;

    @Override
    public void afterPropertiesSet() {

        if (ObjectUtil.isNotNull(enableInvokeTimePrint)) {
            TalbContext.setEnableInvokeTimePrint(enableInvokeTimePrint);
        }

        if (StrUtil.isNotBlank(idGenerator)) {
            try {
                TalbIdGenerator idGenerator = (TalbIdGenerator) TalbSpringAware.registerBean(Class.forName(this.idGenerator));
                TraceIdGeneratorFactory.setIdGenerator(idGenerator);
            } catch (Exception e) {
                throw new RuntimeException("Id生成器包路径不正确");
            }
        }
    }
}
