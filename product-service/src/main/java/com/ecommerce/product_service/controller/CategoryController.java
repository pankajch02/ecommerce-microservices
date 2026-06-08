package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.dto.CreateCategoryRequest;
import com.ecommerce.product_service.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(
            @Valid
            @RequestBody
            CreateCategoryRequest request
    ){
        return categoryService.createCategory(request);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories(){

        return categoryService.getAllCategories();
    }
}
