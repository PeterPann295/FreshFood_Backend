package com.freshfood.controller;

import com.freshfood.dto.request.CategoryRequestDTO;
import com.freshfood.dto.response.ResponseData;
import com.freshfood.model.Category;
import com.freshfood.service.CategoryService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/")
    public ResponseData saveCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return new ResponseData<>(HttpStatus.OK.value(), "Category saved successfully", categoryService.saveCategory(categoryRequestDTO));
    }

    @GetMapping("/list")
    public ResponseData getAllCategories(@RequestParam(defaultValue = "0") @Min(0) int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get list category successfully", categoryService.getAllCategories(pageNo, pageSize));
    }
}
