package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.dto.CreateCategoryRequest;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.exception.CategoryAlreadyExistsException;
import com.ecommerce.product_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;


    public CategoryResponse createCategory(
            CreateCategoryRequest request
    ){
        log.info(
                "Creating category {}",
                request.name()
        );

        if(categoryRepository
                .countByCategoryName(request.name())>0){
            log.warn(
                    "Category already exists {}",
                    request.name()
            );

            throw new CategoryAlreadyExistsException(
                    "Category already exists"
            );
        }

        Category category = Category.builder()
                .name(request.name())
                .description(
                        request.description()
                ).build();

        category = categoryRepository.save(category);

        log.info(
                "Category created successfully id={}",
                category.getId()
        );

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public List<CategoryResponse> getAllCategories(){

        return categoryRepository
                .findAll()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                )).toList();
    }

}
