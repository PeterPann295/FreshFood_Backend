package com.freshfood.service;

import com.freshfood.dto.response.CartResponseDTO;
import com.freshfood.model.Cart;

public interface CartService {
    Cart getCartById(int id);
    int getTotalQuantityByCartId(int id);
    CartResponseDTO getCartResponseById(int id);
}
