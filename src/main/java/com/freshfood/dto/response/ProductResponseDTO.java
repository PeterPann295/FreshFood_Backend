package com.freshfood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freshfood.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private double price;
    private String unit;
    private String thumbnailUrl;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date expiryDate;
    private String status;
    private CategoryResponseDTO category;
    private PromotionResponseDTO promotion;
    private Set<ProductImageResponseDTO> productImages = new HashSet<>();

}
