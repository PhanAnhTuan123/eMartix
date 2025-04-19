package com.eMartix.order_service.order_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {return new ModelMapper();}
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }

}
