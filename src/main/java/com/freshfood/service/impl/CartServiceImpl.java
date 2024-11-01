package com.freshfood.service.impl;

import com.freshfood.model.Cart;
import com.freshfood.repository.CartRepository;
import com.freshfood.service.CartService;
import com.freshfood.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    public Cart getCartById(int id) {
        return findById(id);
    }
    private Cart findById(int id) {
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}
