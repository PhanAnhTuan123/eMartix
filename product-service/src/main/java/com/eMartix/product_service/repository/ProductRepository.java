package com.eMartix.product_service.repository;

import com.eMartix.product_service.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM Products p WHERE " +
            "p.name LIKE CONCAT('%', :name, '%')", nativeQuery = true
    )
    Page<Product> searchProductByName(@Param("name") String name, Pageable pageable);
}
