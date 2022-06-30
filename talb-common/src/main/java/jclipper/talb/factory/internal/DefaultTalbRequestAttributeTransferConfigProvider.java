package jclipper.talb.factory.internal;

import com.google.common.collect.Sets;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.factory.TalbRequestAttributeTransferConfigProvider;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * 默认的{@link TalbRequestAttributeTransferConfigProvider}，默认的白名单为：
 * <ul>
 *     <li>{@link TalbRequest#PREFERRED_IP_KEY}</li>
 *     <li>{@link TalbRequest#PREFERRED_NETWORK_KEY}</li>
 *     <li>{@link TalbRequest#REQUEST_ID_KEY}</li>
 *     <li>{@link TalbRequest#PREFERRED_VERSION_KEY}</li>
 * </ul>
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 19:37.
 */
public class DefaultTalbRequestAttributeTransferConfigProvider implements TalbRequestAttributeTransferConfigProvider {
    @Override
    public Set<String> whiteListKeys() {
        return Sets.newHashSet(TalbRequest.PREFERRED_IP_KEY, TalbRequest.PREFERRED_NETWORK_KEY, TalbRequest.REQUEST_ID_KEY, TalbRequest.PREFERRED_VERSION_KEY);
    }

    @Override
    public Set<String> blackListKeys() {
        return emptySet();
    }
}
