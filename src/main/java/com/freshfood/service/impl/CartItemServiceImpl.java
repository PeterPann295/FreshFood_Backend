package com.freshfood.service.impl;

import com.freshfood.dto.request.CartItemRequestDTO;
import com.freshfood.model.Cart;
import com.freshfood.model.CartItem;
import com.freshfood.model.Product;
import com.freshfood.repository.CartItemRepository;
import com.freshfood.repository.CartRepository;
import com.freshfood.repository.ProductRepository;
import com.freshfood.service.CartItemService;
import com.freshfood.service.CartService;
import com.freshfood.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartService cartService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem getCartItem(int id) {
        return null;
    }

    @Override
    public int saveCartItem(CartItemRequestDTO cartItemRequestDTO) {
        // Lấy cart và product một lần, tránh gọi lại nhiều lần
        Cart cart = cartService.getCartById(cartItemRequestDTO.getCartId());
        Product product = productService.getProduct(cartItemRequestDTO.getProductId());

        // Tìm kiếm CartItem đã tồn tại
        Optional<CartItem> cartItemAvailable = cartItemRepository.findByCartAndProduct(cart, product);

        // Kiểm tra xem CartItem đã tồn tại hay chưa
        if (cartItemAvailable.isPresent()) {
            // Nếu CartItem đã tồn tại, cập nhật số lượng
            CartItem cartItem = cartItemAvailable.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDTO.getQuantity());
            cartItemRepository.save(cartItem);
            return cartItem.getId(); // Trả về ID của CartItem đã cập nhật
        } else {
            // Nếu chưa tồn tại, tạo mới CartItem
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemRequestDTO.getQuantity())
                    .build();
            cartItemRepository.save(cartItem);
            return cartItem.getId(); // Trả về ID của CartItem mới
        }
    }

    @Override
    public void updateCartItem(int id, CartItemRequestDTO cartItemRequestDTO) {

    }

    @Override
    public void deleteCartItem(int id) {

    }



}
