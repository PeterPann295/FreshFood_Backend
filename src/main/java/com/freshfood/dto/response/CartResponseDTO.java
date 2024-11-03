package com.freshfood.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class CartResponseDTO {
    private int id;
    private Set<CartItemReponseDTO> cartItem = new HashSet<CartItemReponseDTO>();
}
