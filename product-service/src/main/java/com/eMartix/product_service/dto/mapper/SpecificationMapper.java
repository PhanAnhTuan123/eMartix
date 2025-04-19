package com.eMartix.product_service.dto.mapper;

import com.eMartix.product_service.dto.model.ProductSpecificationDto;
import com.eMartix.product_service.entity.ProductSpecification;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SpecificationMapper {
    private ModelMapper mapper;

    public ProductSpecificationDto mapToDto(ProductSpecification specification){
        ProductSpecificationDto specificationDto = mapper.map(specification, ProductSpecificationDto.class);
        return specificationDto;
    }

    public ProductSpecification mapToEntity(ProductSpecificationDto specificationDto){
        ProductSpecification specification = mapper.map(specificationDto, ProductSpecification.class);
        return specification;
    }
}
