package com.freshfood.service;

import com.freshfood.dto.request.CartItemRequestDTO;
import com.freshfood.model.CartItem;

public interface CartItemService {
    CartItem getCartItem(int id);
    int saveCartItem(CartItemRequestDTO cartItemRequestDTO);
    void updateCartItem(int id,CartItemRequestDTO cartItemRequestDTO);
    void deleteCartItem(int id);
}
