package com.springcloud.webclient;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomLoadBalancerConfig {
    static String webServerUris = "localhost:5011";

    public static class WebServiceConfig { 
        @Bean
        public ServiceInstanceListSupplier customServiceInstanceListSupplier() {
            return new CustomServiceInstanceListSupplier("myserver", webServerUris);
        }
    }
}