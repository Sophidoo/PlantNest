package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.ProductType;
import com.cybertitans.CyberTitans.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDTO {
    private Long productId;

    @NotEmpty
    @NotNull
    private String productName;
    private String growthHabit;
    private String lightLevel;
    private String waterRequirement;
    private String image;
    private int quantity;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    @NotEmpty
    @NotNull
    private double productPrice;

    private String category;
}
