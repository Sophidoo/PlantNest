package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.ReviewType;
import com.cybertitans.CyberTitans.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReviewDTO {
    private String feedback;
    private double rating;
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType = ReviewType.APPLICATION_REVIEW;
    private User user;
}
