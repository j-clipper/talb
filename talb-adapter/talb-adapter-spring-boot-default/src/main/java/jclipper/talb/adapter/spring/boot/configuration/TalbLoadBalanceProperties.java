package jclipper.talb.adapter.spring.boot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 网关负载均衡配置类
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 10:51.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = TalbLoadBalanceProperties.PREFIX)
public class TalbLoadBalanceProperties implements Serializable {

    public static final String PREFIX = "talb.load-balancer";

    /**
     * 是否是测试环境
     */
    private TestEnv testEnv = new TestEnv();

    private Request request = new Request();

    @Data
    public static class TestEnv {
        /**
         * 是否启用
         */
        private boolean enabled = false;

        /**
         * 允许ServiceInstance的网段集合
         */
        private Set<String> allowNetworks = new HashSet<>();

        /**
         * 禁用ServiceInstance的网段集合
         */
        private Set<String> disableNetworks = new HashSet<>();
    }

    @Data
    public static class Request {

        /**
         * 是否允许设置直接访问ServiceInstance的ip
         */
        private boolean allowPreferredIp = false;
        /**
         * 是否允许优先选择ServiceInstance网段
         */
        private boolean allowPreferredNetwork = false;

        /**
         * 是否允许优先选择ServiceInstance版本号
         */
        private boolean allowPreferredVersion = true;
    }


}
