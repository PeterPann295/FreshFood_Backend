package com.freshfood.service.impl;

import com.freshfood.dto.request.ParentCategoryRequestDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.dto.response.ParentCategoryResponseDTO;
import com.freshfood.model.ParentCategory;
import com.freshfood.repository.ParentCategoryRepository;
import com.freshfood.service.CloudinaryService;
import com.freshfood.service.ParentCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentCategoryServiceImpl implements ParentCategoryService {
    private final ParentCategoryRepository parentCategoryRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public long saveParentCategory(ParentCategoryRequestDTO parentCategoryRequestDTO, String imageUrl) {
        ParentCategory parentCategory = ParentCategory.builder()
                .name(parentCategoryRequestDTO.getName())
                .imageUrl(imageUrl).build();

        parentCategoryRepository.save(parentCategory);
        return parentCategory.getId();
    }

    @Override
    public void updateParentCategory(int id,ParentCategoryRequestDTO parentCategoryRequestDTO, String imageUrl) {
        ParentCategory parentCategory = parentCategoryRepository.findById(id).get();
        parentCategory.setName(parentCategoryRequestDTO.getName());
        if(imageUrl != null) {
            parentCategory.setImageUrl(imageUrl);
        }
        parentCategoryRepository.save(parentCategory);
    }

    @Override
    public void deleteParentCategory(int id) {
        parentCategoryRepository.deleteById(id);
    }

    @Override
    public PageResponse getParentCategory(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ParentCategory> parentCategoryPage = parentCategoryRepository.findAll(pageable);
        List<ParentCategoryResponseDTO> parentCategoryList = parentCategoryPage.stream().map(parent -> ParentCategoryResponseDTO.builder()
                .id(parent.getId())
                .imageUrl(parent.getImageUrl())
                .name(parent.getName())
                .categories(parent.getCategories())
                .build()).toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(parentCategoryPage.getTotalPages())
                .items(parentCategoryList)
                .build();
    }
    @Override
    public ParentCategory findParentCategoryById(int id) {
        return parentCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent category not found"));
    }
}
