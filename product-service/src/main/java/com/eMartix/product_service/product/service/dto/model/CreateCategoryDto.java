package com.eMartix.product_service.product.service.dto.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class CreateCategoryDto {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, message = "Category name should have at least 2 characters!")
    private String name;

    @NotEmpty
    private String thumbnailUrl;
    private Long parentId;
    private boolean isPrimary;

}