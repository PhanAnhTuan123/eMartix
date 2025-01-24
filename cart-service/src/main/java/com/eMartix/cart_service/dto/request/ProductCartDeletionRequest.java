package com.eMartix.cart_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCartDeletionRequest {
    private List<Long> productItemId;
    private Long productId;
}
