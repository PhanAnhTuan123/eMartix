package com.eMartix.noti_service.noti.service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceConfig {
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
