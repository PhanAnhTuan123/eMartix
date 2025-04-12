package com.eMartix.product_service.dto.mapper;


import com.eMartix.product_service.dto.response.ProductWithOptionForCartDto;
import com.eMartix.product_service.entity.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductWithOptionForCartMapper {

    private final ModelMapper mapper;
    public ProductWithOptionForCartDto mapToProductOptionDto(Product product){
        ProductWithOptionForCartDto productWithOptionDto = mapper.map(product, ProductWithOptionForCartDto.class);
        return productWithOptionDto;
    }
}
