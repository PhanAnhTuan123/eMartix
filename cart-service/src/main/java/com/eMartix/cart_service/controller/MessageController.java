package com.eMartix.cart_service.controller;

import com.eMartix.cart_service.service.CartRedisService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/carts")
public class MessageController {
    private CartRedisService cartRedisService;


}
