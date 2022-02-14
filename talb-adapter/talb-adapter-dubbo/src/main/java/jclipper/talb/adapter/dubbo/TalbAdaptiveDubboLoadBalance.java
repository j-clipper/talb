package jclipper.talb.adapter.dubbo;

import jclipper.talb.adapter.dubbo.adapter.DubboInvocationRequest;
import jclipper.talb.adapter.dubbo.adapter.DubboInvoker2InstanceAdapter;
import jclipper.talb.base.Instance;
import jclipper.talb.base.LoadBalancer;
import jclipper.talb.base.TalbRequest;
import jclipper.talb.context.TalbContext;
import jclipper.talb.factory.base.TalbObjectFactory;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 自定义LoadBalance，用于消费侧
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/18 09:34.
 */
public class TalbAdaptiveDubboLoadBalance extends AbstractLoadBalance implements LoadBalance {
    public static final String NAME = "talbAdaptive";

    private final LoadBalance backupLoadBalance;

    public TalbAdaptiveDubboLoadBalance() {
        this.backupLoadBalance = new RandomLoadBalance();
    }

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        LoadBalancer loadBalancer = TalbObjectFactory.getObject(LoadBalancer.class);

        if (loadBalancer == null) {
            return backupLoadBalance.select(invokers, url, invocation);
        }
        List<Invoker> temp = invokers.stream().map(i -> (Invoker) i).collect(toList());
        List<Instance> instances = DubboInvoker2InstanceAdapter.adapters(temp);

        TalbRequest cachedTalbRequest = TalbContext.getLocalContext() == null ? null : TalbContext.getLocalContext().request();

        TalbRequest request = DubboInvocationRequest.adapter(invocation);

        if (cachedTalbRequest != null && cachedTalbRequest.getAttributes() != null && cachedTalbRequest.getAttributes().size() > 0) {
            //上游的cachedTalbRequest.getAttributes传递给request.attributes和Invocation.attachments
            request.getAttributes().putAll(cachedTalbRequest.getAttributes());
        }

        Instance choose = loadBalancer.choose(instances, request);

        return (Invoker<T>) choose.original();
    }

}
