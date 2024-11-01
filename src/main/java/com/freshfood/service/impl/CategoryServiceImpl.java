package com.freshfood.service.impl;

import com.freshfood.dto.request.CategoryRequestDTO;
import com.freshfood.dto.response.CategoryResponseDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.model.Category;
import com.freshfood.repository.CategoryRepository;
import com.freshfood.service.CategoryService;
import com.freshfood.service.ParentCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ParentCategoryService parentCategoryService;
    @Override
    public int saveCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = Category.builder()
                .name(categoryRequestDTO.getName())
                .parentCategory(parentCategoryService.findParentCategoryById(categoryRequestDTO.getParentCategoryId()))
                .build();
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    public void updateCategory(int id, CategoryRequestDTO categoryRequestDTO) {

    }

    @Override
    public void deleteCategory(int id, CategoryRequestDTO categoryRequestDTO) {

    }

    @Override
    public PageResponse getAllCategories(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryResponseDTO> categoryResponseDTOList = categoryPage.stream().map(category -> CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build()).toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(categoryPage.getTotalPages())
                .items(categoryResponseDTOList)
                .build();
    }

    @Override
    public CategoryResponseDTO getCategoryById(int id) {
        return null;
    }

    @Override
    public Category getCateById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
