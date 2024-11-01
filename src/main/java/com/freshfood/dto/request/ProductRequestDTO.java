package com.freshfood.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freshfood.dto.validator.EnumPattern;
import com.freshfood.model.Category;
import com.freshfood.model.Promotion;
import com.freshfood.util.ProductStatus;
import com.freshfood.util.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
public class ProductRequestDTO implements Serializable {
    @NotBlank(message = "name must be not blank")
    private String name;

    @NotNull(message = "description must be not null")
    private String description;

    @NotNull(message = "price must be not null")
    @Min(1000)
    private double price;

    @EnumPattern(name = "unit", regexp = "KG|G|L|ML|BOX|CAN|BOTTLE|PIECE|BAG|BUNDLE|PACK")
    private Unit unit;

    @NotNull(message = "Expiry date must be not null")
    @JsonFormat(pattern = "MM/dd/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date expiryDate;

    @EnumPattern(name = "status", regexp = "AVAILABLE|OUT_OF_STOCK|DISCONTINUED|PRE_ORDER|ARCHIVED")
    private ProductStatus status;

    private int categoryId;

    private int promotionId;

}
