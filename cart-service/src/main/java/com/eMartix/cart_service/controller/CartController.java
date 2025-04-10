package com.eMartix.cart_service.controller;

import com.eMartix.cart_service.dto.model.ProductDto;
import com.eMartix.cart_service.dto.request.CartItemRequest;
import com.eMartix.cart_service.dto.request.ProductCartDeletionRequest;
import com.eMartix.cart_service.dto.request.UpdateCartRequest;
import com.eMartix.cart_service.service.CartRedisService;
import com.eMartix.commons.utils.CustomHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Cart",
        description = "REST APIs for Cart"
)

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private CartRedisService cartRedisService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProductsFromCart(@RequestHeader(CustomHeaders.X_AUTH_USER_ID) String userId){
        return new ResponseEntity<>(this.cartRedisService.getProductsFromCart(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addProductToCart(
            @RequestHeader(CustomHeaders.X_AUTH_USER_ID) String userId,
            @RequestBody CartItemRequest item
    ){
        this.cartRedisService.addProductToCart(userId, item);
        return new ResponseEntity<>("Add to cart successfully!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateProductInCart(
            @RequestHeader(CustomHeaders.X_AUTH_USER_ID) String userId,
            @RequestBody UpdateCartRequest item
    ){
        this.cartRedisService.updateProductInCart(userId, item);

        return new ResponseEntity<>("Update cart successfully!", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProductInCart(
            @RequestHeader(CustomHeaders.X_AUTH_USER_ID) String userId,
            @RequestBody ProductCartDeletionRequest item
    ){
        this.cartRedisService.deleteProductInCart(userId, item);
        return new ResponseEntity<>("Product delete from cart successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllProductsInCart(
            @RequestHeader(CustomHeaders.X_AUTH_USER_ID) String userId
    ){
        this.cartRedisService.deleteAllProductsInCart(userId);
        return new ResponseEntity<>("Delete all products in cart successfully!", HttpStatus.OK);
    }

    @GetMapping("/info")
    public String getUserInfo(@RequestHeader(CustomHeaders.X_AUTH_USER_ID) String userId,
                              @RequestHeader(CustomHeaders.X_AUTH_USER_AUTHORITIES) String authorities) {
        // Xử lý logic dựa trên thông tin người dùng trong header của yêu cầu
        return "User ID: " + userId + ", Authorities: " + authorities;
    }
}
