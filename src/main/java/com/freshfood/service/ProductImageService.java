package com.freshfood.service;

import com.freshfood.dto.request.ProductImageRequestDTO;
import com.freshfood.dto.request.ProductRequestDTO;
import com.freshfood.model.ProductImage;

public interface ProductImageService {
    long saveProductImage(ProductImageRequestDTO productImageRequestDTO);
}
