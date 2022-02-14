package jclipper.talb.id;

import cn.hutool.core.util.IdUtil;
import jclipper.talb.base.TalbIdGenerator;

/**
 * @author wf2311
 */
public class TalbDefaultIdGenerator implements TalbIdGenerator {

    @Override
    public String generate() {
        return IdUtil.getSnowflake().nextIdStr();
    }
}
