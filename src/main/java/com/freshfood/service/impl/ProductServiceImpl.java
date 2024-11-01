package com.freshfood.service.impl;

import com.freshfood.dto.request.ProductRequestDTO;
import com.freshfood.dto.response.CategoryResponseDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.dto.response.ProductImageResponseDTO;
import com.freshfood.dto.response.ProductResponseDTO;
import com.freshfood.model.Product;
import com.freshfood.model.ProductImage;
import com.freshfood.repository.CategoryRepository;
import com.freshfood.repository.ProductImageRepository;
import com.freshfood.repository.ProductRepository;
import com.freshfood.repository.search.SearchProductRepository;
import com.freshfood.service.CategoryService;
import com.freshfood.service.ProductImageService;
import com.freshfood.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SearchProductRepository searchProductRepository;
    @Override
    public long saveProduct(ProductRequestDTO productRequestDTO, String thumbnailUrl , String[] imageUrl) {

        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .thumbnailUrl(thumbnailUrl)
                .unit(productRequestDTO.getUnit())
                .category(categoryService.getCateById(productRequestDTO.getCategoryId()))
                .expiryDate(productRequestDTO.getExpiryDate())
                .status(productRequestDTO.getStatus())
                .build();
        product.setProductImages(convertToProductImage(imageUrl, product));
        productRepository.save(product);
        return product.getId();
    }

    @Override
    public void updateProduct(int id, ProductRequestDTO productRequestDTO, String[] imageUrl) {

    }

    @Override
    public void deleteProduct(int id) {

    }

    @Override
    public Product getProduct(int id) {
        return getProById(id);
    }

    private Product getProById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("product not found"));
    }

    @Override
    public PageResponse getProducts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDTO> productResponseDTOS = products.stream().map(product -> ProductResponseDTO.builder()
                .category(new CategoryResponseDTO(product.getCategory()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .thumbnailUrl(product.getThumbnailUrl())
                .unit(product.getUnit().toString())
                .expiryDate(product.getExpiryDate())
                .id(product.getId())
                .status(product.getStatus().toString())
                .productImages((Set<ProductImageResponseDTO>) product.getProductImages().stream().map(productImage -> ProductImageResponseDTO.builder()
                        .id(productImage.getId())
                        .imageUrl(productImage.getImageUrl())
                        .altText(productImage.getAltText())
                        .build()).collect(Collectors.toSet()))
                .build()).toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(products.getTotalPages())
                .items(productResponseDTOS)
                .build();
    }

    @Override
    public ProductResponseDTO getProductById(int id) {
        Product product = getProById(id);
        return ProductResponseDTO.builder()
                .category(new CategoryResponseDTO(product.getCategory()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .unit(product.getUnit().toString())
                .thumbnailUrl(product.getThumbnailUrl())
                .expiryDate(product.getExpiryDate())
                .id(product.getId())
                .status(product.getStatus().toString())
                .productImages((Set<ProductImageResponseDTO>) product.getProductImages().stream().map(productImage -> ProductImageResponseDTO.builder()
                        .id(productImage.getId())
                        .imageUrl(productImage.getImageUrl())
                        .altText(productImage.getAltText())
                        .build()).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public PageResponse<?> getAllProductsWithSortAndSearch(int pageNo, int pageSize, String sort, String search) {
        return searchProductRepository.getAllProductsWithSortAndSearch(pageNo,pageSize,sort,search);
    }

    @Override
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String category, String sort, String... search) {
        return searchProductRepository.advanceSearchByCriteria(pageNo,pageSize,category,sort,search);
    }

    @Override
    public PageResponse<?> advanceSearchWithSpecification(Pageable pageable, String[] product, String[] category) {
        return searchProductRepository.advanceSearchWithSpecification(pageable, product, category);
    }

    private HashSet<ProductImage> convertToProductImage(String[] urlImage, Product product) {
        HashSet<ProductImage> productImages = new HashSet<>();
        for (int i = 0; i < urlImage.length; i++) {
            productImages.add(ProductImage.builder()
                            .imageUrl(urlImage[i])
                            .altText("products")
                            .product(product)
                    .build());
        }
        return productImages;
    }

}
