package com.eMartix.product_service.controller;

import com.eMartix.commons.utils.AppContants;


import com.eMartix.product_service.dto.message.CategoryResponseDto;
import com.eMartix.product_service.dto.model.CategoryDto;
import com.eMartix.product_service.dto.model.CreateCategoryDto;
import com.eMartix.product_service.dto.response.ObjectResponse;
import com.eMartix.product_service.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Category",
        description = "REST APIs for Product"
)
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private CategoryService categoryService;
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryDto createCategoryDto){
        return new ResponseEntity<>(this.categoryService.createCategory(createCategoryDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ObjectResponse<CategoryResponseDto>> getAllCategories(
            @RequestParam(value = "pageNo", defaultValue = AppContants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppContants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppContants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(this.categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(name = "categoryId") Long id){
        return new ResponseEntity<>(this.categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable(name = "categoryId") Long id,
            @RequestBody CreateCategoryDto createCategoryDto
    ){
        return new ResponseEntity<>(this.categoryService.updateCategory(createCategoryDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable(name = "categoryId") Long id
    ){
        this.categoryService.deleteCategory(id);
        return new ResponseEntity<>("Delete category successfully!", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ObjectResponse<CategoryResponseDto>> searchCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "pageNo", defaultValue = AppContants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppContants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppContants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppContants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(this.categoryService.searchCategory(name, pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
}
