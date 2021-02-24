package com.springcloud.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@LoadBalancerClient(value = "myserver", configuration = CustomLoadBalancerConfig.class)
public class WebClientController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    WebClientController(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.lbFunction = lbFunction;
    }

    @Autowired
    private WebClient webClient;
    
    @GetMapping("/test")
    public Mono<String> doTest() {
        WebClient client = WebClient.create();
        return client.get()
            .uri("http://localhost:5011/webclient/test-create")
            .retrieve()
            .bodyToMono(String.class);
    }

    @GetMapping("/test2")
    public Mono<String> doTest2() {
        return webClient.get()
                .uri("http://localhost:5011/webclient/test-builder")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/test3")
    public Mono<String> doTest3() {
        return webClient
                .mutate() 
                .filter(lbFunction)
                //.baseUrl("http://localhost:5011")
                .baseUrl("http://myserver")
                .build()
                .get()
                .uri("/webclient/test-mutate")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/test4")    
    public Mono<String> doTest4() {
        WebClient client = WebClient.builder()
            .filter(this.lbFunction)
            .baseUrl("http://webserver")
            .build();

        return client.get()
            .uri("/webclient/test-builder2")
            .retrieve()
            .bodyToMono(String.class);
    }    
}
