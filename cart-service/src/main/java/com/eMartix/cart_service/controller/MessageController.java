package com.eMartix.cart_service.controller;

import com.eMartix.cart_service.service.CartRedisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class MessageController {

    @Value("${spring.boot.message}")
    private String message;

    @GetMapping("/carts/message")
    public String getMessage(){
        return message;
    }
}