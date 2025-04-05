package com.eMartix.product_service.product.service.service;


import com.eMartix.product_service.product.service.dto.message.CategoryResponseDto;
import com.eMartix.product_service.product.service.dto.model.CategoryDto;
import com.eMartix.product_service.product.service.dto.model.CreateCategoryDto;
import com.eMartix.product_service.product.service.dto.response.ObjectResponse;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryDto categoryDto);
    CategoryDto getCategoryById(Long id);
    ObjectResponse<CategoryResponseDto> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);
    CategoryDto updateCategory(CreateCategoryDto categoryDto, Long id);
    void deleteCategory(Long id);
    ObjectResponse<CategoryResponseDto> searchCategory(String name, int pageNo, int pageSize, String sortBy, String sortDir);
}
