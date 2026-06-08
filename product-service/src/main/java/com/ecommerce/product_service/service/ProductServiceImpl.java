package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CreateProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.dto.UpdateProductRequest;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(
            CreateProductRequest request
    ){

        log.info(
                "Creating product {}",
                request.name()
        );

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException(
                        "Category not found"
                ));

        Product product = Product.builder()
                .name(request.name())
                .description(
                        request.description()
                )
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .category(category)
                .build();

        product = productRepository.save(product);

        log.info(
                "Product created id={}",
                product.getId()
        );

        return mapToResponse(product);
    }

    public ProductResponse getProductById(Long id){

        log.info(
                "Fetching product id={}",
                id
        );

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found"
                ));

        return mapToResponse(product);
    }

    public Page<ProductResponse> getAllProducts(
            int page,
            int size,
            String sortBy
    ){
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        Page<Product> products = productRepository.findAll(pageable);

        return products.map(
                this::mapToResponse
        );
    }

    public List<ProductResponse> searchProducts(String keyword){
        log.info(
                "Searching products {}",
                keyword
        );

        return productRepository
                .searchProducts(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse updateProduct(
            Long productId,
            UpdateProductRequest request
    ){
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found"
                ));

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Category not found"
                        )
                );

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(category);

        product = productRepository.save(product);

        log.info("Product updated id={}",
                product.getId());

        return mapToResponse(product);

    }

    public void deleteProduct(Long id){

        Product product = productRepository.
                findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found"
                ));

        productRepository.delete(product);

        log.info("Product deleted id={}",
                id);
    }

    private ProductResponse mapToResponse(Product product){

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }

}
