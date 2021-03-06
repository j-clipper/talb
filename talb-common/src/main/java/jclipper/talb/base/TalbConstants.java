package jclipper.talb.base;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:33.
 */
public interface TalbConstants {

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

    String WEBFLUX_EXCHANGE = "exchange";

    String TALB_CONTEXT = "talbServerContext";

    String TALB_REQUEST = "talbServerRequest";

    String TALB_CONTEXT_ATTRS_FOR_NEXT = "talbContextAttrsForNext";

    String HTTP_SERVLET_REQUEST = "httpServletRequest";

    String INSTANCE_FILTER_MARKED_KEY = "_slb_instance_filter_marked_list";

    String SPRING_APP_NAME_KEY = "spring.application.name";

}
