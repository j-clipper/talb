package com.wf2311.talb.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.wf2311.talb.base.RpcType;
import com.wf2311.talb.base.TalbConstants;
import com.wf2311.talb.base.TalbRequest;
import com.wf2311.talb.context.trace.TraceInfo;
import com.wf2311.talb.factory.AppInfoConfigProvider;
import com.wf2311.talb.factory.TalbRequestAttributeTransferConfigProvider;
import com.wf2311.talb.factory.base.TalbObjectFactory;
import com.wf2311.talb.id.TraceIdGeneratorFactory;
import com.wf2311.talb.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.wf2311.talb.base.TalbConstants.INIT_SPAN_ID;
import static java.util.Collections.emptyMap;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 17:32.
 */
@Slf4j
public class TalbContext extends TraceInfo<TalbContext> {

    private static final TransmittableThreadLocal<TalbContext> SERVER_CONTEXT = new TransmittableThreadLocal<>();
    private static final TransmittableThreadLocal<TalbContext> LOCAL_CONTEXT = new TransmittableThreadLocal<>();


    private static final Logger TRACE_LOGGER = LoggerFactory.getLogger("traceRecord");

    private static boolean enableInvokeTimePrint = false;


    public static void setEnableInvokeTimePrint(boolean enableInvokeTimePrint) {
        TalbContext.enableInvokeTimePrint = enableInvokeTimePrint;
    }


    public static boolean enableInvokeTimePrint() {
        return enableInvokeTimePrint;
    }

    /**
     * 使用AppInfoConfigProvider 获取当前应用的名称，如果AppInfoConfigProvider不存在，则返回NONE
     *
     * @return 当前应用的名称
     */
    public static String getAppName() {
        AppInfoConfigProvider provider = TalbObjectFactory.getObject(AppInfoConfigProvider.class);
        if (provider == null) {
            return TalbConstants.UNKNOWN;
        }
        return provider.getAppName();
    }

    /**
     * 生成传递给下游应用的TraceInfo信息
     *
     * @return
     */
    public static Map<String, String> getTraceMapForNext() {
        TalbContext context = getLocalContext();
        if (context == null) {
            return emptyMap();
        }
        return context.toMapForNext(NextTalbContextGenerator.generateNextSpanId());
    }

    /**
     * 打印当前TalbContext的信息
     *
     * @param context 当前TalbContext
     */
    private static void printCurrentContext(TalbContext context) {
        if (context == null) {
            TRACE_LOGGER.warn("context attributes is null");
            return;
        }
        if (TRACE_LOGGER.isDebugEnabled()) {
            context = getLocalContext();
            Map<String, Object> temp = new HashMap<>(4);
            temp.put("url", context.url());
            temp.put("trace", context.toMap());
            temp.put("request", context.request() == null ? null : context.request().getAttributes());
            TRACE_LOGGER.debug(JsonUtils.toJsonString(temp));
        }
    }

    /**
     * 获取当前TalbContext的TalbRequest中可以用于传递给下游服务的attributes
     *
     * @return TalbRequest.attributes
     */
    public static Map<String, String> getRequestAttributesForNext() {
        return getRequestAttributesForNext(getLocalContext());
    }

    /**
     * 获取TalbContext的TalbRequest中可以用于传递给下游服务的attributes
     *
     * @param context TalbContext
     * @return TalbRequest.attributes
     */
    public static Map<String, String> getRequestAttributesForNext(TalbContext context) {
        if (context == null || context.request() == null) {
            return emptyMap();
        }
        TalbRequest request = context.request();
        TalbRequestAttributeTransferConfigProvider provider = TalbObjectFactory.getObject(TalbRequestAttributeTransferConfigProvider.class);
        if (provider == null) {

            if (log.isWarnEnabled()) {
                log.warn("not found instance object of TalbRequestAttributeTransferConfigProvider");
            }
            return emptyMap();
        }
        return provider.filteredAttributes(request);
    }


    /**
     * 获取当前服务的TalbContext
     *
     * @return
     */
    public static TalbContext getServerContext() {
        return SERVER_CONTEXT.get();
    }

    /**
     * 创建来自上游服务的TalbContext
     *
     * @param traceId       traceId
     * @param spanIdForNext 上游服务给当前服务生成的spanId
     * @param prevAppName   上游服务的应用名称
     * @return 上游服务的TalbContext
     */
    public static TalbContext newServerContext(String traceId, String spanIdForNext, String prevAppName) {
        TalbContext context = new TalbContext();
        if (traceId == null) {
            traceId = TraceIdGeneratorFactory.get().generate();
            if (prevAppName != null && log.isDebugEnabled()) {
                log.debug("[TALB]可能上一个节点[{}]没有正确传递traceId,重新生成traceId[{}]", prevAppName, traceId);
            }
        }
        if (spanIdForNext == null) {
            spanIdForNext = INIT_SPAN_ID;
        }
        context.traceId(traceId).spanIdForNext(spanIdForNext).appName(prevAppName);
        SERVER_CONTEXT.set(context);
        return context;
    }


    /**
     * 获取当前服务的TalbContext
     *
     * @return
     */
    public static TalbContext getLocalContext() {
        return LOCAL_CONTEXT.get();
    }

    /**
     * 创建当前服务的TalbContext
     *
     * @param appName 当前服务的应用名称
     * @param rpcType 当前服务的rpc类型
     * @return TalbContext
     */
    public static TalbContext newLocalContext(String appName, RpcType rpcType) {
        TalbContext context = TalbContext.getLocalContext();
        if (context != null && context.traceId() != null) {
            return context;
        }
        RpcType currentRpcType = rpcType != null ? rpcType : RpcType.UNKNOWN;
        context = new TalbContext()
                .traceId(TalbContext.getServerContext().traceId())
                .spanId(TalbContext.getServerContext().spanIdForNext())
                .rpcType(NextTalbContextGenerator.generateRpcType(currentRpcType))
                .appName(NextTalbContextGenerator.generateService(appName != null ? appName : TalbConstants.UNKNOWN))
                .host(NextTalbContextGenerator.generateHost())
                .ip(NextTalbContextGenerator.generateIp());
        LOCAL_CONTEXT.set(context);

        printCurrentContext(context);
        return context;
    }

    /**
     * 清除所有缓存
     */
    public static void clearContext() {
        SERVER_CONTEXT.remove();
        LOCAL_CONTEXT.remove();
        NextTalbContextGenerator.removeSpanIndexCache();
    }

    /**
     * 清除当前context的缓存
     */
    public static void clearLocalContext() {
        LOCAL_CONTEXT.remove();
    }

}
