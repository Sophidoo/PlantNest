package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.dto.ApplicationReviewDTO;
import com.cybertitans.CyberTitans.dto.ProductReviewDTO;
import com.cybertitans.CyberTitans.dto.ReviewResponseDTO;
import com.cybertitans.CyberTitans.model.Reviews;
import com.cybertitans.CyberTitans.service.ReviewService;
import com.cybertitans.CyberTitans.utls.AppConstant;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/reviews")
@Tag(
        name = " REVIEW REST API",
        description = "Thise section contains endpoints for user submitting reviews and adming viewing all reviews"
)
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/product-review/{productId}")
    public ResponseEntity<String> giveProductReview(@RequestBody ProductReviewDTO productReviewDTO, @PathVariable Long productId){
        String review = reviewService.giveproductReview(productReviewDTO, productId);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/application-review")
    public ResponseEntity<String> giveApplicationReview(@RequestBody ApplicationReviewDTO applicationReviewDTO){
        String review = reviewService.giveApplicationReview(applicationReviewDTO);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/all-reviews")
    public ResponseEntity<ReviewResponseDTO> getAllReviews(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        ReviewResponseDTO allReviews = reviewService.getAllReviews(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allReviews, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'USER')")
    @GetMapping("/product-reviews")
    public ResponseEntity<ReviewResponseDTO> getAllProductReviews(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        ReviewResponseDTO allReviews = reviewService.getAllProductReviews(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allReviews, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/application-reviews")
    public ResponseEntity<ReviewResponseDTO> getAllApplicationReviews(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        ReviewResponseDTO allReviews = reviewService.getAllApplicationReviews(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allReviews, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/single-product-review/{productId}")
    public ResponseEntity<ReviewResponseDTO> getAParticularProductReviews(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @PathVariable Long productId
    ){
        ReviewResponseDTO allReviews = reviewService.getAllReviewForAParticularProduct(pageNo, pageSize, sortBy, sortDir, productId);
        return new ResponseEntity<>(allReviews, HttpStatus.OK);
    }
}
