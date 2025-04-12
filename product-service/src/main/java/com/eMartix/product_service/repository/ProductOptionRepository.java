package com.eMartix.product_service.repository;

import com.eMartix.product_service.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProductId(Long productId);
    @Query("SELECT o FROM ProductOption o WHERE o.product.id = :productId AND o.name = :name")
    List<ProductOption> findByProductIdAndName(@Param("productId") Long productId, @Param("name") String name);

}
