package jclipper.talb.base;

import lombok.Getter;

import java.util.Arrays;

/**
 * 远程调用类型
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/14 19:37.
 */
@Getter
public enum RpcType {
    /**
     * 未知
     */
    UNKNOWN("UNKNOWN"),
    /**
     * spring cloud gateway
     */
    SPRING_CLOUD_GATEWAY("SCG"),
    /**
     * spring webflux
     */
    SPRING_WEBFLUX("SWF"),

    /**
     * spring mvc
     */
    SPRING_MVC("MVC"),
    /**
     * dubbo
     */
    DUBBO("D"),

    /**
     * feign
     */
    FEIGN("F"),

    /**
     * spring rest template
     */
    SPRING_REST_TEMPLATE("SRT"),
    ;

    RpcType(String code) {
        this.code = code;
    }

    String code;

    /**
     * 根据code获取对应的RpcType
     *
     * @param code code
     * @return RpcType
     */
    public static RpcType getByCode(String code) {
        return Arrays.stream(RpcType.values()).filter(e -> e.code.equalsIgnoreCase(code)).findFirst().orElse(null);
    }
}
