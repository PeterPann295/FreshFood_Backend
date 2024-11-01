package com.freshfood.dto.response;

import com.freshfood.model.Promotion;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponseDTO implements Serializable {
    private int id;
    private String name;
    private int discountPercentage;
    private Date startDate;
    private Date endDate;
    public PromotionResponseDTO(Promotion promotion) {
        this.id = promotion.getId();
        this.name = promotion.getName();
        this.discountPercentage = promotion.getDiscountPercentage();
        this.startDate = promotion.getStartDate();
        this.endDate = promotion.getEndDate();
    }
}
