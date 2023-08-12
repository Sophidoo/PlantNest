package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.ProductType;
import com.cybertitans.CyberTitans.enums.ReviewType;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.User;
import jakarta.persistence.Entity;

import jakarta.validation.constraints.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDTO {
    private String feedback;

    @Size(min = 1, max = 5)
    private double rating;
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType = ReviewType.PRODUCT_REVIEW;
    private User user;
    private Product product;
}
