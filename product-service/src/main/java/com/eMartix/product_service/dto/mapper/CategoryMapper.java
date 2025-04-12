package com.eMartix.product_service.dto.mapper;


import com.eMartix.product_service.dto.message.CategoryResponseDto;
import com.eMartix.product_service.dto.model.CategoryDto;
import com.eMartix.product_service.entity.Category;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryMapper {
    private ModelMapper mapper;
    public CategoryDto mapToDto(Category category){
        return mapper.map(category, CategoryDto.class);
    }

    public Category mapToEntity(CategoryDto categoryDto){
        return mapper.map(categoryDto, Category.class);
    }

    public CategoryResponseDto mapToResponseDto(Category category){
        return mapper.map(category, CategoryResponseDto.class);
    }

    public Category mapToResponseEntity(CategoryResponseDto categoryResponseDto){
        return mapper.map(categoryResponseDto, Category.class);
    }
}
