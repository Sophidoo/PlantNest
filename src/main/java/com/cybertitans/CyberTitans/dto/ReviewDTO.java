package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.ReviewType;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewDTO {
    private String feedback;
    private double rating;
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;
    private LocalDate date = LocalDate.now();
    private Product product;
    private User user;
}
