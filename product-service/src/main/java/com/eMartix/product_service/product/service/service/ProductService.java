package com.eMartix.product_service.product.service.service;


import com.eMartix.product_service.product.service.dto.message.ProductDetailResponseDto;
import com.eMartix.product_service.product.service.dto.model.ProductDto;
import com.eMartix.product_service.product.service.dto.response.ObjectResponse;
import com.eMartix.product_service.product.service.dto.response.ProductWithOptionForCartDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ObjectResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductDetailResponseDto getProductById(Long id);
    ProductDto updateProduct(ProductDto productDto, Long productId);
    void deleteProduct(Long productId);

    ProductWithOptionForCartDto getProductByProductOptionId(String productOptionId);
    ObjectResponse<ProductDto> searchProduct(String name, int pageNo, int pageSize, String sortBy, String sortDir);
}
