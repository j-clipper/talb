package jclipper.talb.request.search.impl;

import com.google.common.base.Strings;
import jclipper.talb.base.Instance;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.request.search.InstanceVersionSearcher;

import java.util.Map;

/**
 * 默认的{@link InstanceVersionSearcher}实现类,会从{@link Instance#getMetadata()}中查找key={@link TalbConstants#INSTANCE_VERSION_KEY}的值
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/3/3 16:48.
 */
public class DefaultInstanceVersionSearcher implements InstanceVersionSearcher {
    @Override
    public String search(Instance instance) {
        Map<String, String> metadata = instance.getMetadata();
        if (metadata == null) {
            return null;
        }
        String version = metadata.get(TalbConstants.INSTANCE_VERSION_KEY);
        if (Strings.isNullOrEmpty(version)) {
            return null;
        }
        return version;
    }
}
