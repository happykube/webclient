package com.springcloud.webclient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import reactor.core.publisher.Flux;

public class CustomServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    private final String serviceId;
    private final String webServerUris;

    CustomServiceInstanceListSupplier(String serviceId, String webServerUris) {
        this.serviceId = serviceId;
        this.webServerUris = webServerUris;
    }


    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List lists = new ArrayList();
        String[] uris = webServerUris.split(",");
        for(String uri:uris) {
            String[] tmp = uri.split(":");
            lists.add(new DefaultServiceInstance(serviceId + lists.size(), serviceId, tmp[0], Integer.parseInt(tmp[1]), false));
        }

        return Flux.just(lists);        
    }
}