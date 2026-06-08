package com.ecommerce.product_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String extractEmail(String token){

        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token){

        try{
            SecretKey key = Keys.hmacShaKeyFor(
                    secretKey.getBytes()
            );

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e){

            return false;
        }
    }

    public String extractRole(
            String token
    ){
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get(
                "role",
                String.class
        );
    }
}
