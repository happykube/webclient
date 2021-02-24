package com.springcloud.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class WebClientController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    WebClientController(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.lbFunction = lbFunction;
    }

    @Autowired
    private WebClient webClient;

    @GetMapping("/testscl2")
    public Mono<String> testSCL2() {
        return webClient
                .mutate() 
                .filter(lbFunction)
                .baseUrl("http://webserver")
                .build()
                .get()
                .uri("/webclient/test SCL2")
                .retrieve()
                .bodyToMono(String.class);
    }    
}
