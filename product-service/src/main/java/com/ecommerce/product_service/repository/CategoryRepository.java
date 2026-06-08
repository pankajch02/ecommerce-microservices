package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category,Long> {


    @Query(
            value = """
            SELECT COUNT(*)
            FROM categories
            WHERE name = :name
            """,
            nativeQuery = true
    )
    long countByCategoryName(String name);
}
