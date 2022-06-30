package jclipper.talb.id;

import cn.hutool.core.util.IdUtil;
import jclipper.talb.base.TalbIdGenerator;

/**
 * 默认的ID生成器，通过雪花算法生成ID
 *
 * @author wf2311
 */
public class TalbDefaultIdGenerator implements TalbIdGenerator {

    @Override
    public String generate() {
        return IdUtil.getSnowflake().nextIdStr();
    }
}
