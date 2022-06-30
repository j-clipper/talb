package jclipper.talb.trace.dubbo;

import jclipper.talb.base.RpcType;
import jclipper.talb.base.TalbAttributesRequest;
import jclipper.talb.base.TalbConstants;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.context.TalbContext;
import jclipper.talb.factory.TalbRequestAttributeTransferConfigProvider;
import jclipper.talb.factory.base.TalbObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 处理Table Trace 信息在Dubbo中消费侧的传递和在服务端的获取
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/17 22:20.
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = -10000)
@Slf4j
public class TalbTraceDubboFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result;
        String side = invoker.getUrl().getParameter(CommonConstants.SIDE_KEY);
        String appName = TalbContext.getAppName();

        if (side.equals(CommonConstants.PROVIDER_SIDE)) {
            String prevRpcType = invocation.getAttachment(TalbConstants.PREV_RPC_TYPE_KEY);
            String prevService = invocation.getAttachment(TalbConstants.PREV_SERVICE_KEY);
            String prevHost = invocation.getAttachment(TalbConstants.PREV_HOST);
            String prevIp = invocation.getAttachment(TalbConstants.PREV_IP_KEY);
            String traceId = invocation.getAttachment(TalbConstants.TRACE_KEY);
            String spanId = invocation.getAttachment(TalbConstants.SPAN_ID_KEY);

            TalbRequest talbRequest = parseAttributeToRequest(invocation);

            //设置从消费者传递的信息中设置上下文
            TalbContext.newServerContext(traceId, spanId, prevService)
                    .rpcType(prevRpcType)
                    .host(prevHost)
                    .ip(prevIp)
                    .request(talbRequest);

            TalbContext.newLocalContext(appName, RpcType.DUBBO).request(talbRequest);

            try {
                //调用dubbo
                result = invoker.invoke(invocation);
            } finally {
                String protocolServiceKey = invocation.getProtocolServiceKey();
                if (protocolServiceKey == null || !protocolServiceKey.endsWith("injvm")) {
                    TalbContext.clearContext();
                }
            }

            return result;
        } else if (side.equals(CommonConstants.CONSUMER_SIDE)) {

            Map<String, String> traceAttributes = TalbContext.getTraceMapForNext();
            Map<String, String> requestAttributes = TalbContext.getRequestAttributesForNext();

            if (traceAttributes.size() > 0) {
                traceAttributes.forEach(RpcContext.getContext()::setAttachment);
            }
            if (requestAttributes != null && requestAttributes.size() > 0) {
                requestAttributes.forEach(RpcContext.getContext()::setAttachment);
            }
            result = invoker.invoke(invocation);
        } else {
            result = null;
        }
        return result;
    }


    /**
     * 将上游请求的 TalbRequest.attribute 解析出来
     *
     * @param invocation ServerWebExchange
     * @return TalbAttributesRequest
     */
    private TalbRequest parseAttributeToRequest(Invocation invocation) {
        TalbRequestAttributeTransferConfigProvider provider = TalbObjectFactory.getObject(TalbRequestAttributeTransferConfigProvider.class);
        if (provider == null || provider.whiteListKeys() == null || provider.whiteListKeys().size() == 0) {
            return null;
        }
        Set<String> keys = provider.whiteListKeys();
        Map<String, Object> attributes = new HashMap<>(keys.size());
        keys.forEach(key -> {
            String value = invocation.getAttachment(key);
            if (value != null) {
                attributes.put(key, value);
            }
        });
        return new TalbAttributesRequest(attributes);
    }
}