package com.freshfood.dto.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CategoryRequestDTO implements Serializable {
    private String name;
    private int parentCategoryId;
}
