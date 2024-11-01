package com.freshfood.dto.response;

import com.freshfood.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageResponseDTO implements Serializable {
    private long id;
    private String imageUrl;
    private String altText;
    public ProductImageResponseDTO(ProductImage image) {
        this.id = image.getId();
        this.imageUrl = image.getImageUrl();
        this.altText = image.getAltText();
    }
}
