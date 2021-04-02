package com.springcloud.webclient;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
public class Controller {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    Controller(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.lbFunction = lbFunction;
    }

    @GetMapping("/testscl1")    
    @ApiOperation( value = "webclient sample", notes="WebClient로 L/B하는 예")
    public Mono<String> testSCL1() {
        WebClient client = WebClient.builder()
            .filter(this.lbFunction)
            .baseUrl("http://webserver")
            .build();

        return client.get()
            .uri("/webclient/test SCL1")
            .retrieve()
            .bodyToMono(String.class);
    }    
}
