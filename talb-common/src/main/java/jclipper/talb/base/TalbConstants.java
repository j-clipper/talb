package jclipper.talb.base;

/**
 * Talb常量
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:33.
 */
public interface TalbConstants {

    /**
     * 初始SpanId值
     */
    String INIT_SPAN_ID = "0";

    String TRACE_KEY = "traceId";

    String SPAN_ID_KEY = "spanId";

    String PREV_SERVICE_KEY = "prevService";

    String PREV_HOST = "prevHost";

    String PREV_IP_KEY = "prevIp";

    String PREV_RPC_TYPE_KEY = "prevRpcType";

    String SERVICE_KEY = "service";

    String HOST_KEY = "host";

    String IP_KEY = "ip";

    String RPC_TYPE_KEY = "rpcType";

    String UNKNOWN = "NONE";

    String TALB_CONTEXT = "talbServerContext";

    String TALB_REQUEST = "talbServerRequest";

    String TALB_CONTEXT_ATTRS_FOR_NEXT = "talbContextAttrsForNext";

    String INSTANCE_FILTER_MARKED_KEY = "_slb_instance_filter_marked_list";

    String SPRING_APP_NAME_KEY = "spring.application.name";

    String INSTANCE_VERSION_KEY = "version";

}
