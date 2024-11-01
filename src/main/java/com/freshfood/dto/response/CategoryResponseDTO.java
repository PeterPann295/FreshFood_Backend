package com.freshfood.dto.response;

import com.freshfood.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CategoryResponseDTO implements Serializable {
    private int id;
    private String name;
    public CategoryResponseDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

}
