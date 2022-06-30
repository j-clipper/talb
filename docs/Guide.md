# Trace And LoadBalance 
链路追踪和负载均衡，适配SpringCloud Gateway、SpringMVC、Dubbo、Feign

## 使用方式
最新版本号：
```xml
<talb.version>2022.2-SNAPSHOT</talb.version>
```
### Maven 依赖
- `Spring Cloud Gateway`
```xml
<dependency>
    <groupId>cn.strong</groupId>
    <artifactId>talb-gateway-spring-cloud-starter</artifactId>
    <version>${talb.version}</version>
</dependency>
```
- `Spring Boot`
```xml
<dependency>
    <groupId>cn.strong</groupId>
    <artifactId>talb-web-spring-cloud-starter</artifactId>
    <version>${talb.version}</version>
</dependency>
```

- `Spring Cloud OpenFeign`
```xml
<dependency>
    <groupId>cn.strong</groupId>
    <artifactId>talb-openfeign-spring-cloud-starter</artifactId>
    <version>${talb.version}</version>
</dependency>
```
- `Dubbo`
```xml
<dependency>
    <groupId>cn.strong</groupId>
    <artifactId>talb-adapter-dubbo</artifactId>
    <version>${talb.version}</version>
</dependency>
```

### 项目配置

```yaml
talb:
  load-balancer:
    request:
      allow-direct-ip: false #是否允许设置直接访问ServiceInstance的ip，默认为false
      allow-preferred-networks: false #是否允许优先选择ServiceInstance网段，默认为false
    test-env:
      enabled: false #是否开启测试环境，默认为false。当开启时，将按照allow-networks和disable-networks配置的网段进行进行全局过滤。
      allow-networks: #允许ServiceInstance的网段集合，默认为空。优先级低于disable-networks
        - 192.168.20
        - 192.168.21
      disable-networks: #允许ServiceInstance的网段集合，默认为空。优先级高于allow-networks
        - 192.168.150
        - 192.168.151
```

### 服务实例过滤器
#### 全局的网段过滤
应用场景：例如在测试环境中对网段进行限定或过滤，防止开发者的注册的实例被调用   
对服务实例的ip按照 `GlobalPreferredNetworkConfigProvider` 中的`allowNetworks`、`disableNetworks`进行过滤  
该过滤器是否启用由`GlobalPreferredNetworkConfigProvider.isEnabled()`进行控制，`true`表示进行过滤，`false`表示不进行过滤  

实例过滤是按照配置的服务网段进行过滤的，分为白名单和黑名单，顺序如下：
1. 白名单：由`GlobalPreferredNetworkConfigProvider.getAllowNetworks()`提供，保留白名单中匹配实例，如果都不匹配，则保留所有实例
2. 黑名单：由`GlobalPreferredNetworkConfigProvider.getDisableNetworks()`提供，移除黑名单中匹配的实例，如果过滤后保留的实例列表为空，则回滚过滤保留上一部中所有的实例
   例如存在如下几个服务实例， 
IP分别为:
```text
   192.168.3.10
   192.168.3.11
   192.168.4.10
   192.168.5.10
   ```
过滤顺序：
1白名单列表：
```text
   192.168.3
   192.168.4
```
保留匹配实例:
```text
   192.168.3.10
   192.168.3.11
   192.168.4.10
``` 
2. 黑名单列表：
```text
   192.168.90
``` 
   无匹配，保留所有实例
```text
   192.168.3.10
   192.168.3.11
   192.168.4.10
``` 

#### 根据请求端偏好的服务实例Ip对服务实例进行选择过滤
该过滤器是否启用由 `RequestPreferredIpConfigProvider.isAllowPreferredIp()` 进行控制，true表示进行过滤，false表示不进行过滤  
进行匹配时，如果存在匹配实例，则返回匹配实例；如果没有匹配条件(指定的Ip)或者无匹配实例，则不进行过滤，返回所有实例

例如存在如下几个服务实例，IP分别为
```text
192.168.3.10
192.168.3.11
192.168.4.10
192.168.5.10
``` 
情况一：如果根据 `RequestPreferredIpAddressSearcher.search(TalbRequest)` 找到的请求这偏好的IP为
```text
192.168.3.10
192.168.3.11
``` 
则ip=192.168.3.10、ip=192.168.3.11的两个实例会被选中保留
情况二：如果根据 `RequestPreferredIpAddressSearcher.search(TalbRequest)` 找到的请求这偏好的IP为
```text
192.168.6.10
``` 
没有匹配的实例，则不进行过滤，返回所有的实例
情况三：如果根据 `RequestPreferredIpAddressSearcher.search(TalbRequest)` 没有找到的携带的请求这偏好的IP信息 则也不进行过滤，返回所有的实例

#### 根据请求端偏好的服务实例网段对服务实例进行过滤
该过滤器是否启用由 `RequestPreferredNetworkConfigProvider.isAllowPreferredNetworks()`进行控制，true表示进行过滤，false表示不进行过滤
进行匹配时，如果存在匹配实例，则返回匹配实例；如果没有匹配条件(指定的网段)或者无匹配实例，则不进行过滤，返回所有实例
例如存在如下几个服务实例，IP分别为
```text
192.168.3.10
192.168.3.11
192.168.4.10
192.168.5.10
```
情况一：如果根据 `RequestPreferredNetworksSearcher.search(TalbRequest)` 找到的请求者偏好的网段为
```text
192.168.3
192.168.5
```
则ip=192.168.3.10、ip=192.168.3.11、ip=192.168.5.10的三个实例会被选中保留
情况二：如果根据 `RequestPreferredNetworksSearcher.search(TalbRequest)` 找到的请求者偏好的网段为
```text
192.168.6
```
没有匹配的实例，则不进行过滤，返回所有的实例
情况三：如果根据 `RequestPreferredNetworksSearcher.search(TalbRequest)` 没有找到的携带的请求这偏好的网段信息 则也不进行过滤，返回所有的实例

#### 根据请求端偏好的服务实例版本号对服务实例进行过滤
该过滤器是否启用由 `RequestPreferredVersionConfigProvider.isAllowPreferredVersion()` 进行控制，true表示进行过滤，false表示不进行过滤。 
使用 `InstanceVersionSearcher.search(Instance)`对服务实例中携带的版本号进行查找  
进行匹配时，如果存在匹配实例，则返回匹配实例；如果没有匹配条件(指定的版本号)或者无匹配实例，则不进行过滤，返回所有实例  
匹配是支持前缀匹配的，即如果偏好的版本号为`a.b.*`，则以`a.b.`开头的版本号实例都会被匹配到
例如存在如下几个服务实例，其版本号分别为
```text
1.1.2
1.1.3
1.2.1
```

情况一：如果根据 `RequestPreferredVersionSearcher.search(TalbRequest)` 找到的请求者偏好的服务实例版本号为
```text
1.1.*
1.2.1
```
则version=1.1.2、version=1.1.3、version=1.2.1的三个实例会被选中保留
情况二：如果根据 `RequestPreferredVersionSearcher.search(TalbRequest)` 找到的请求者偏好的实例版本号为
```text
1.3.*
```
没有匹配的实例，则不进行过滤，返回所有的实例
情况三：如果根据 `RequestPreferredVersionSearcher.search(TalbRequest)` 没有找到的携带的请求这偏好的实例版本号信息 则也不进行过滤，返回所有的实例

### 负载均衡策略

#### 一致性哈希负载均衡器
具体实现参见 `ConsistentHashLoadBalancer`

#### 轮询负载均衡器
具体实现参见 `RoundRobinLoadBalancer`

#### 使用服务实例过滤器的负载均衡器
具体实现参见 `FilterInstanceLoadBalancer`，该负载均衡器先根据 `InstanceFilter` 进行实例过滤，再根据过滤结果进行负载均衡

默认的负载均衡器为 `FilterInstanceLoadBalancer`，`FilterInstanceLoadBalancer`会默认使用 `ConsistentHashLoadBalancer`作为代理。

### TalbRequest中的相关属性设值及读取说明
- `__g_preferred_ip`
  请求中携带的直接访问ServiceInstance的ip的key，会依次在Header、Cookie、QueryParam中进行查找  
  多个IP之间通过英文逗号分割，例如：`_g_preferred_ip=192.168.150.78,192.168.151.78`
- `_g_preferred_network`
  请求中携带的优先选择ServiceInstance网段的key，会依次在Header、Cookie、QueryParam中进行查找  
  多个IP之间通过英文逗号分割，例如：`_g_preferred_network=192.168.150,192.168.151`
- `_g_service_version`
  请求中携带的优先选择ServiceInstance版本号的解压，会依次在Header、Cookie、QueryParam中进行查找 
  多个版本号之间通过英文逗号分割，例如：`_g_service_version=1.1.2,1.1.3`
- `_g_request_id`
  请求中携带的requestId的key，会依次在Header、Cookie、Attributes中进行查找
  例如：`_g_request_id=123456789`



