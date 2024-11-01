package com.freshfood.dto.request;

import com.freshfood.model.Cart;
import com.freshfood.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class CartItemRequestDTO implements Serializable {
    private int cartId;

    private int productId;

    private int quantity;
}
