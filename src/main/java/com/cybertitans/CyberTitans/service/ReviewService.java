package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.ApplicationReviewDTO;
import com.cybertitans.CyberTitans.dto.ProductReviewDTO;
import com.cybertitans.CyberTitans.dto.ReviewDTO;
import com.cybertitans.CyberTitans.dto.ReviewResponseDTO;
import com.cybertitans.CyberTitans.model.Reviews;

import java.util.List;

public interface ReviewService {
    String giveproductReview(ProductReviewDTO productReviewDTO, Long productId);
    String giveApplicationReview(ApplicationReviewDTO applicationReviewDTO);
    ReviewResponseDTO getAllReviews(int pageNo, int pageSize, String sortBy, String sortDir);
    ReviewResponseDTO getAllProductReviews(int pageNo, int pageSize, String sortBy, String sortDir);
    ReviewResponseDTO getAllApplicationReviews(int pageNo, int pageSize, String sortBy, String sortDir);
    ReviewResponseDTO getAllReviewForAParticularProduct(int pageNo, int pageSize, String sortBy, String sortDir, Long productId);
}
