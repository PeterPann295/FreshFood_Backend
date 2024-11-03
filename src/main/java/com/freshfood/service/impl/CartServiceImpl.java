package com.freshfood.service.impl;

import com.freshfood.dto.response.*;
import com.freshfood.model.Cart;
import com.freshfood.model.CartItem;
import com.freshfood.model.Product;
import com.freshfood.repository.CartRepository;
import com.freshfood.service.CartService;
import com.freshfood.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    public Cart getCartById(int id) {
        return findById(id);
    }

    @Override
    public int getTotalQuantityByCartId(int id) {
        return cartRepository.getTotalQuantityByCartId(id).orElse(0);
    }

    @Override
    public CartResponseDTO getCartResponseById(int id) {
        Cart cart = findById(id);
        Set<CartItem> cartItems = cart.getCartItems();
        Set<CartItemReponseDTO> cartItemReponseDTOS = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            cartItemReponseDTOS.add(CartItemReponseDTO.builder()
                            .id(cartItem.getId())
                            .productDTO(convertToProductResponse(cartItem.getProduct()))
                            .quantity(cartItem.getQuantity())
                    .build());
        }
        return CartResponseDTO.builder()
                .id(cart.getId())
                .cartItem(cartItemReponseDTOS)
                .build();
    }

    private Cart findById(int id) {
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
    }
    public ProductResponseDTO convertToProductResponse(Product product) {
        return ProductResponseDTO.builder()
                .category(new CategoryResponseDTO(product.getCategory()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .unit(product.getUnit().toString())
                .thumbnailUrl(product.getThumbnailUrl())
                .expiryDate(product.getExpiryDate())
                .id(product.getId())
                .status(product.getStatus().toString())
                .productImages((Set<ProductImageResponseDTO>) product.getProductImages().stream().map(productImage -> ProductImageResponseDTO.builder()
                        .id(productImage.getId())
                        .imageUrl(productImage.getImageUrl())
                        .altText(productImage.getAltText())
                        .build()).collect(Collectors.toSet()))
                .build();
    }
}
