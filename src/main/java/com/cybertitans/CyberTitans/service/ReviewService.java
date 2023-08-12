package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.ApplicationReviewDTO;
import com.cybertitans.CyberTitans.dto.ProductReviewDTO;
import com.cybertitans.CyberTitans.dto.ReviewDTO;
import com.cybertitans.CyberTitans.dto.ReviewResponseDTO;
import com.cybertitans.CyberTitans.enums.ProductType;
import com.cybertitans.CyberTitans.model.Reviews;

import java.util.List;

public interface ReviewService {
    String giveproductReview(ProductReviewDTO productReviewDTO, Long productId);
    String giveApplicationReview(ApplicationReviewDTO applicationReviewDTO);
    List<Reviews> getAllReviews();
    List<Reviews> getAllProductReviews();
    List<Reviews> getAllApplicationReviews();
    List<Reviews> getAllReviewForAParticularProduct(Long productId);
}
