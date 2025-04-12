package com.eMartix.product_service.repository;



import com.eMartix.product_service.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM Categories c WHERE " +
            "c.url_key LIKE CONCAT('%', :urlKey, '%')", nativeQuery = true
    )
    Category findByUrlKey(@Param("urlKey") String urlKey);

    @Query(value = "SELECT * FROM Categories c WHERE " +
            "c.name LIKE CONCAT('%', :name, '%')", nativeQuery = true
    )
    Page<Category> searchCategoriesByName(@Param("name") String name, Pageable pageable);
}
