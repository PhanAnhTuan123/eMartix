package com.eMartix.cart_service.service;

import com.eMartix.cart_service.dto.model.ProductDto;
import com.eMartix.cart_service.dto.request.CartItemRequest;
import com.eMartix.cart_service.dto.request.ProductCartDeletionRequest;
import com.eMartix.cart_service.dto.request.UpdateCartRequest;
import com.eMartix.cart_service.service.base.BaseRedisService;

import java.util.List;

public interface CartRedisService extends BaseRedisService {
    void addProductToCart(String userId, CartItemRequest item);
    void updateProductInCart(String userId, UpdateCartRequest item);
    void deleteProductInCart(String userId, ProductCartDeletionRequest request);
    void deleteAllProductsInCart(String userId);
    List<ProductDto> getProductsFromCart(String userId);

}
