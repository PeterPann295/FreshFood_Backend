package com.freshfood.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemReponseDTO {
    private int id;
    private int quantity;
    private ProductResponseDTO productDTO;
}
