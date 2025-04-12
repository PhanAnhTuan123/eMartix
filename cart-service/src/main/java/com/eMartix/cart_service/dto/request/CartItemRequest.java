package com.eMartix.cart_service.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Data
public class CartItemRequest {
    private List<Long> productItemId;
    private Long productId;
    private int quantity;
}
