package com.ecommerce.product_service.repository;


import com.ecommerce.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


    @Query(
            value =
            """
            SELECT *
            FROM products
            WHERE LOWER(name)
            LIKE LOWER(CONCAT('%',:keyword, '%'))
            """,
            nativeQuery = true
    )
    List<Product> searchProducts(String keyword);
}
