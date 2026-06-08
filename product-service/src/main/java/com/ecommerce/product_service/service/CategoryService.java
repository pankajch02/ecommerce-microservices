package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.dto.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(
            CreateCategoryRequest request
    );

    List<CategoryResponse> getAllCategories();

}
