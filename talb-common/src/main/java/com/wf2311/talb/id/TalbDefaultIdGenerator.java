package com.wf2311.talb.id;

import cn.hutool.core.util.IdUtil;
import com.wf2311.talb.base.TalbIdGenerator;

/**
 * @author wf2311
 */
public class TalbDefaultIdGenerator implements TalbIdGenerator {

    @Override
    public String generate() {
        return IdUtil.getSnowflake().nextIdStr();
    }
}
