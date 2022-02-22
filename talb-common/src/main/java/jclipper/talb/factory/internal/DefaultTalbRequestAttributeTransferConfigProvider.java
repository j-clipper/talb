package jclipper.talb.factory.internal;

import com.google.common.collect.Sets;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.TalbRequestAttributeTransferConfigProvider;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 19:37.
 */
public class DefaultTalbRequestAttributeTransferConfigProvider implements TalbRequestAttributeTransferConfigProvider {
    @Override
    public Set<String> whiteListKeys() {
        return Sets.newHashSet(TalbRequest.PREFERRED_IP_KEY, TalbRequest.PREFERRED_NETWORK_KEY, TalbRequest.REQUEST_ID_KEY,TalbRequest.PREFERRED_VERSION_KEY);
    }

    @Override
    public Set<String> blackListKeys() {
        return emptySet();
    }
}
