package jclipper.talb.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import jclipper.talb.base.RpcType;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.utils.LocalhostUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/15 16:09.
 */
public class NextTalbContextGenerator {

    protected static final boolean APPEND_PREVIOUS = true;

    public static final String APPEND_SPLIT = ">";


    public static volatile TransmittableThreadLocal<AtomicInteger> spanIndex = new TransmittableThreadLocal<>();


    public static void removeSpanIndexCache() {
        spanIndex.remove();
    }

    public static String generateNextSpanId() {
        String currentSpanId = TalbContext.getLocalContext().spanId();
        AtomicInteger index = spanIndex.get();
        if (index == null) {
            index = new AtomicInteger(0);
            spanIndex.set(index);
        }
        int currentSpanIndex = index.incrementAndGet();
        return String.format("%s.%d", currentSpanId, currentSpanIndex);
    }

    public static String generateRpcType(RpcType currentRpcType) {
        return generateProperty(TalbContext.getServerContext().rpcType(), currentRpcType == null ? null : currentRpcType.getCode());
    }

    public static String generateService(String service) {
        return generateProperty(TalbContext.getServerContext().appName(), service);
    }

    public static String generateHost() {
        return generateProperty(TalbContext.getServerContext().host(), LocalhostUtil.getHostName());
    }

    public static String generateIp() {
        return generateProperty(TalbContext.getServerContext().ip(), LocalhostUtil.getHostIp());
    }

    private static String generateProperty(String prevProperty, String property) {
        String p = property == null ? TalbConstants.UNKNOWN : property;
        if (!APPEND_PREVIOUS || prevProperty == null) {
            return p;
        }

        return String.format("%s%s%s", prevProperty, APPEND_SPLIT, property);
    }
}
