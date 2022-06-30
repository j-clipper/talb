package jclipper.talb.context.trace;

import jclipper.talb.base.TalbConstants;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.context.TalbContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TraceInfo，保存上下游服务实例的一些
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/21 16:19.
 */
@NoArgsConstructor
@AllArgsConstructor
public class TraceInfo<T> implements Serializable {
    /**
     * TraceID
     */
    private String traceId;

    /**
     * SpanID
     */
    private String spanId;

    /**
     * 调用链上的下一个SpanID
     */
    private String spanIdForNext;
    /**
     * 当前服务的host
     */
    private String host;
    /**
     * 当前服务的ip
     */
    private String ip;
    /**
     * 当前服务的名称
     */
    private String appName;
    /**
     * Rpc调用类型
     */
    private String rpcType;
    /**
     * 当前调用的URL
     */
    private transient String url;

    /**
     * Talb请求
     */
    private TalbRequest request;

    /**
     * 获取 traceId 的值
     *
     * @see TraceInfo#traceId
     */
    public String traceId() {
        return traceId;
    }

    /**
     * 设置 traceId 的值
     *
     * @see TraceInfo#traceId
     */
    public T traceId(String traceId) {
        this.traceId = traceId;
        return (T) this;
    }

    /**
     * 获取 spanId 的值
     *
     * @see TraceInfo#spanId
     */
    public String spanId() {
        return spanId;
    }

    /**
     * 设置 spanId 的值
     *
     * @see TraceInfo#spanId
     */
    public T spanId(String spanId) {
        this.spanId = spanId;
        return (T) this;
    }

    /**
     * 获取 spanIdForNext 的值
     *
     * @see TraceInfo#spanIdForNext
     */
    public String spanIdForNext() {
        return spanIdForNext;
    }

    /**
     * 设置 spanIdForNext 的值
     *
     * @see TraceInfo#spanIdForNext
     */
    public T spanIdForNext(String spanIdForNext) {
        this.spanIdForNext = spanIdForNext;
        return (T) this;
    }

    /**
     * 获取 host 的值
     *
     * @see TraceInfo#host
     */
    public String host() {
        return host;
    }

    /**
     * 设置 host 的值
     *
     * @see TraceInfo#host
     */
    public T host(String host) {
        this.host = host;
        return (T) this;
    }

    /**
     * 获取 ip 的值
     *
     * @see TraceInfo#ip
     */
    public String ip() {
        return ip;
    }

    /**
     * 设置 ip 的值
     *
     * @see TraceInfo#ip
     */
    public T ip(String ip) {
        this.ip = ip;
        return (T) this;
    }

    /**
     * 获取 appName 的值
     *
     * @see TraceInfo#appName
     */
    public String appName() {
        return appName;
    }

    /**
     * 设置 appName 的值
     *
     * @see TraceInfo#appName
     */
    public T appName(String appName) {
        this.appName = appName;
        return (T) this;
    }

    /**
     * 获取 rpcType 的值
     *
     * @see TraceInfo#rpcType
     */
    public String rpcType() {
        return rpcType;
    }

    /**
     * 设置 rpcType 的值
     *
     * @see TraceInfo#rpcType
     */
    public T rpcType(String rpcType) {
        this.rpcType = rpcType;
        return (T) this;
    }

    /**
     * 获取 request 的值
     *
     * @see TraceInfo#request
     */
    public TalbRequest request() {
        return request;
    }

    /**
     * 设置 request 的值
     *
     * @see TraceInfo#request
     */
    public T request(TalbRequest request) {
        this.request = request;
        return (T) this;
    }

    /**
     * 获取 url 的值
     *
     * @see TraceInfo#url
     */
    public String url() {
        return url;
    }

    /**
     * 设置 url 的值
     *
     * @see TraceInfo#url
     */
    public T url(String url) {
        this.url = url;
        return (T) this;
    }


    /**
     * 为调用链的下一个调用构造TraceInfo
     *
     * @param nextSpanId 下一个调用的SpanID
     * @return Trace Info Map
     */
    public Map<String, String> toMapForNext(String nextSpanId) {
        Map<String, String> map = new HashMap<>(8);
        spanIdForNext = nextSpanId;
        map.put(TalbConstants.TRACE_KEY, TalbContext.getLocalContext().traceId());
        map.put(TalbConstants.SPAN_ID_KEY, spanIdForNext);
        map.put(TalbConstants.PREV_IP_KEY, ip);
        map.put(TalbConstants.PREV_HOST, host);
        map.put(TalbConstants.PREV_SERVICE_KEY, appName);
        map.put(TalbConstants.PREV_RPC_TYPE_KEY, rpcType);
        return map;
    }

    /**
     * 将当前TraceInfo转换为Map
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>(8);
        map.put(TalbConstants.TRACE_KEY, traceId);
        map.put(TalbConstants.SPAN_ID_KEY, spanId);
        map.put(TalbConstants.IP_KEY, ip);
        map.put(TalbConstants.HOST_KEY, host);
        map.put(TalbConstants.SERVICE_KEY, appName);
        map.put(TalbConstants.RPC_TYPE_KEY, rpcType);
        return map;
    }
}
