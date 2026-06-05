package com.ecommerce.auth_service.repository;

import com.ecommerce.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query(
            value = """
        SELECT *
        FROM users
        WHERE email = :email
        """,
            nativeQuery = true
    )
    Optional<User> findByEmail(String email);

    @Query(
            value = """
        SELECT COUNT(*)
        FROM users
        WHERE email = :email
        """,
            nativeQuery = true
    )
    long countByEmail(String email);
}
