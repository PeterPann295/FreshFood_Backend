package com.freshfood.service;

import com.freshfood.dto.request.CategoryRequestDTO;
import com.freshfood.dto.response.CategoryResponseDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.model.Category;

public interface CategoryService {
    int saveCategory(CategoryRequestDTO categoryRequestDTO);
    void updateCategory(int id, CategoryRequestDTO categoryRequestDTO);
    void deleteCategory(int id, CategoryRequestDTO categoryRequestDTO);
    PageResponse getAllCategories(int pageNo, int pageSize);
    CategoryResponseDTO getCategoryById(int id);
    Category getCateById(int id);
}
