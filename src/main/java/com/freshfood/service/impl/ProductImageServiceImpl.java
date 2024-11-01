package com.freshfood.service.impl;

import com.freshfood.dto.request.ProductImageRequestDTO;
import com.freshfood.dto.request.ProductRequestDTO;
import com.freshfood.model.ProductImage;
import com.freshfood.repository.ProductImageRepository;
import com.freshfood.service.ParentCategoryService;
import com.freshfood.service.ProductImageService;
import com.freshfood.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductService productService;

    @Override
    public long saveProductImage(ProductImageRequestDTO productImageRequestDTO) {
        return 0;
    }
}
