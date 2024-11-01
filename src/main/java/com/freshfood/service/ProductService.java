package com.freshfood.service;

import com.freshfood.dto.request.ProductRequestDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.dto.response.ProductResponseDTO;
import com.freshfood.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    long saveProduct(ProductRequestDTO productRequestDTO, String thumbnailUrl , String[] imageUrl);
    void updateProduct(int id, ProductRequestDTO productRequestDTO, String[] imageUrl);
    void deleteProduct(int id);
    Product getProduct(int id);
    PageResponse getProducts(int pageNo, int pageSize);
    ProductResponseDTO getProductById(int id);
    PageResponse<?> getAllProductsWithSortAndSearch(int pageNo, int pageSize, String sort, String search);
    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize,String category, String sort, String... search);
    public PageResponse<?> advanceSearchWithSpecification(Pageable pageable, String[] product, String[] category);
}
