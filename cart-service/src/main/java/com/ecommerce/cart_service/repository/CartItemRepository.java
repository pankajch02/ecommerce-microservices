package com.ecommerce.cart_service.repository;

import com.ecommerce.cart_service.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductId(
            Long cartId,
            Long productId
    );

    @Modifying
    @Transactional
    @Query(
            value = """
                    DELETE
                    FROM cart_items
                    WHERE cart_id = :cartId
                    """,
            nativeQuery = true
    )
    void deleteAllByCartId(Long cartId);


}
