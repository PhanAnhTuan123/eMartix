package com.eMartix.cart_service.service.impl;

import com.eMartix.cart_service.dto.model.ProductDto;
import com.eMartix.cart_service.dto.request.CartItemRequest;
import com.eMartix.cart_service.dto.request.ProductCartDeletionRequest;
import com.eMartix.cart_service.dto.request.UpdateCartRequest;
import com.eMartix.cart_service.service.CartRedisService;
import com.eMartix.cart_service.service.base.impl.BaseRedisServiceImpl;
import com.eMartix.commons.advice.ResourceNotFoundException;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class CartRedisServiceImpl extends BaseRedisServiceImpl implements CartRedisService { private final WebClient webClient;

    @Autowired
    public CartRedisServiceImpl(RedisTemplate<String, Object> redisTemplate, WebClient webClient){
        super(redisTemplate);
        this.webClient = webClient;
    }

    @Override
    public void addProductToCart(String userId, CartItemRequest item) {
        String key = "cart:user-" + userId;
        StringBuilder fieldKeyBuilder;
        int updateQuantity;
        if(Objects.nonNull(item.getProductItemId())){

            fieldKeyBuilder = new StringBuilder("product_item:");
            boolean isFirst = true;
            for (Long optionId : item.getProductItemId()) {
                if (!isFirst) {
                    fieldKeyBuilder.append(",");
                } else {
                    isFirst = false;
                }
                fieldKeyBuilder.append(optionId);
            }

        } else {
            fieldKeyBuilder = new StringBuilder("product:");
            fieldKeyBuilder.append(item.getProductId());
        }

        // Ep kieu ve lai String de thao tac
        String fieldKey = fieldKeyBuilder.toString();

        if (this.hashExist(userId, fieldKey)) {
            updateQuantity = (Integer) this.hashGet(userId, fieldKey) + item.getQuantity();
        } else {
            updateQuantity = item.getQuantity();
        }

        this.hashSet(key, fieldKey, updateQuantity);
    }

    @Override
    public void updateProductInCart(String userId, UpdateCartRequest item) {
        String key = "cart:user-" + userId;
        String fieldKey;
        long delta = item.getDelta();

        if(Objects.nonNull(item.getProductItemId())){
            // Tao 1 stringBuilder de noi chuoi
            StringBuilder fieldKeyBuilder = new StringBuilder("product_item:");

            // logic xu ly tuong tu voi vong for trong ham addToCart
            boolean isFirst = true;
            for(Long optionId : item.getProductItemId()){
                if (!isFirst) {
                    fieldKeyBuilder.append(",");
                } else {
                    isFirst = false;
                }
                fieldKeyBuilder.append(optionId);
            }

            fieldKey = fieldKeyBuilder.toString();

        } else {
            fieldKey = "product:" + item.getProductId();
        }

        Long quantityLeft = this.hashIncrBy(key, fieldKey, delta);

        if(quantityLeft <= 0){
            this.delete(key, fieldKey);
        }
    }

    @Override
    public void deleteProductInCart(String userId, ProductCartDeletionRequest request) {
        if(Objects.nonNull(request.getProductItemId())){
            StringBuilder fieldKeyBuilder = new StringBuilder("product_item:");
            boolean isFirst = true;
            for(Long optionId : request.getProductItemId()){
                if (!isFirst) {
                    fieldKeyBuilder.append(",");
                } else {
                    isFirst = false;
                }
                fieldKeyBuilder.append(optionId);
            }

            this.checkFieldKeyExist("cart:user-" + userId, fieldKeyBuilder.toString());
            this.delete("cart:user-" + userId,  fieldKeyBuilder.toString());
        } else {
            this.checkFieldKeyExist("cart:user-" + userId, "product:" + request.getProductId());
            this.delete("cart:user-" + userId, "product:" + request.getProductId());
        }
    }

    @Override
    public void deleteAllProductsInCart(String userId){
        this.delete("cart:user-" + userId);
    }

    @Override
    public List<ProductDto> getProductsFromCart(String userId) {
        String key = "cart:user-" + userId;
        Map<String, Object> products = this.getField(key);

        List<ProductDto> productList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : products.entrySet()) {
            boolean isProductItem;
            String[] arrKey = entry.getKey().split(":");
            isProductItem = arrKey[0].equals("product_item");
            ProductDto productDto = getProductById(arrKey[1], isProductItem);

            if (productDto != null) {
                int quantity = (int)this.hashGet(key, entry.getKey());
                productDto.setQuantity(quantity);
                productList.add(productDto);
            }
        }
        productList.sort(Comparator.comparing(ProductDto::getId));
        return productList;
    }

    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultProduct")
    private ProductDto getProductById(String id, boolean isProductItem) {
        ProductDto productDto;
        if(isProductItem){
            productDto = this.webClient.post()
                    .uri("http://localhost:8081/api/v1/products/product-options")
                    .bodyValue(id)
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .block();
        } else {
            productDto = this.webClient.get()
                    .uri("http://localhost:8081/api/v1/products/" + id)
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .block();
        }

        return productDto;
    }


    private void checkFieldKeyExist(String key, String keyField){
        if(!this.hashExist(key, keyField)){
            throw new ResourceNotFoundException(key, keyField, 0);
        }
    }

    private ProductDto getDefaultProduct(String id, boolean isProductItem) {
        ProductDto productDto = new ProductDto();
        productDto.setName("Default product");
        productDto.setQuantity(1);
        productDto.setId((long) -1);
        productDto.setOption(new ArrayList<>());
        productDto.setPrice(999999);
        productDto.setDiscountRate(0);
        productDto.setThumbnailUrl("https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/g/r/group_61_1_.png");

        return productDto;
    }
}
