package com.freshfood.controller;

import com.freshfood.dto.response.ResponseData;
import com.freshfood.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseData getCartById(@PathVariable int id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get cart successful", cartService.getCartResponseById(id));
    }
    @GetMapping("/get-quantity/{id}")
    public ResponseData getQuantityCartItemByCartId(@PathVariable int id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get quantity cart-item successful", cartService.getTotalQuantityByCartId(id));
    }
}
