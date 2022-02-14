package jclipper.talb.adapter.spring.cloud.loadbalancer;

import jclipper.talb.adapter.spring.cloud.adapter.SpringCloudRequestDataRequest;
import jclipper.talb.adapter.spring.cloud.adapter.SpringCloudServiceInstance2InstanceAdapter;
import jclipper.talb.base.Instance;
import jclipper.talb.base.LoadBalancer;
import jclipper.talb.base.TalbRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/12 10:16.
 */
@Slf4j
public class TalbReactorLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final String serviceId;

    private final LoadBalancer loadBalancer;

    public TalbReactorLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                   String serviceId, LoadBalancer loadBalancer) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(request, supplier, serviceInstances));
    }

    public Mono<Response<ServiceInstance>> choose(Request request, TalbRequest talbRequest) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, talbRequest));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances, TalbRequest talbRequest) {
//        if(serviceInstances=)
        List<Instance> adapterInstances = SpringCloudServiceInstance2InstanceAdapter.adapters(serviceInstances);
        Instance choose = loadBalancer.choose(adapterInstances, talbRequest);

        Response<ServiceInstance> serviceInstanceResponse = new DefaultResponse((ServiceInstance) choose.original());
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> processInstanceResponse(Request<?> request, ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Object context = request.getContext();
        RequestData requestData = ((RequestDataContext) context).getClientRequest();

        TalbRequest talbRequest = SpringCloudRequestDataRequest.adapter(serviceId, requestData);

        return processInstanceResponse(supplier, serviceInstances, talbRequest);
    }

}
