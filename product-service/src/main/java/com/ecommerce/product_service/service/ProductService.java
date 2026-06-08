package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CreateProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.dto.UpdateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(
            CreateProductRequest request
    );

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(
            int page,
            int size,
            String sortBy
    );

    List<ProductResponse> searchProducts(
            String keyword
    );

    ProductResponse updateProduct(
            Long productId,
            UpdateProductRequest request
    );

    void deleteProduct(Long id);


}
