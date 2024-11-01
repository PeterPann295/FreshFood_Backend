package com.freshfood.controller;

import com.freshfood.dto.request.CartItemRequestDTO;
import com.freshfood.dto.response.ResponseData;
import com.freshfood.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cart-item")
@RestController
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping("/")
    public ResponseData addCartItem(@RequestBody CartItemRequestDTO cartItem) {
        return new ResponseData(HttpStatus.OK.value(), "Add cart item success", cartItemService.saveCartItem(cartItem));
    }
}
