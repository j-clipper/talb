package com.wf2311.talb.base;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 19:37.
 */
@Getter
public enum RpcType {
    /**
     *
     */
    UNKNOWN("UNKNOWN"),
    SPRING_CLOUD_GATEWAY("SCG"),
    SPRING_WEBFLUX("SWF"),
    SPRING_MVC("MVC"),
    DUBBO("D"),
    FEIGN("F"),
    SPRING_REST_TEMPLATE("SRT"),
    ;

    RpcType(String code) {
        this.code = code;
    }

    String code;

    public static RpcType getByCode(String code) {
        return Arrays.stream(RpcType.values()).filter(e -> e.code.equalsIgnoreCase(code)).findFirst().orElse(null);
    }
}
