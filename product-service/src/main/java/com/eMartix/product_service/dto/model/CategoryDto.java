package com.eMartix.product_service.dto.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CategoryDto {
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;
    private String urlKey;
    private String thumbnailUrl;
    private Long parentId;
    private Set<ProductDto> products;
}
