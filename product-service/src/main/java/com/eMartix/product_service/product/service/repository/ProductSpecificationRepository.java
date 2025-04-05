package com.eMartix.product_service.product.service.repository;


import com.eMartix.product_service.product.service.entity.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
}
