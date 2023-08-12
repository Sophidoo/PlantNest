package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.enums.ReviewType;
import com.cybertitans.CyberTitans.exception.ResourceNotFoundException;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.Reviews;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.ProductRepository;
import com.cybertitans.CyberTitans.repository.ReviewRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository, ModelMapper mapper, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    public String giveproductReview(ProductReviewDTO productReviewDTO, Long productId) {
        User user = getLoggedInUser();
        Reviews reviews = new Reviews();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        reviews.setReviewType(ReviewType.PRODUCT_REVIEW);
        reviews.setFeedback(productReviewDTO.getFeedback());
        reviews.setRating(productReviewDTO.getRating());
        reviews.setUser(user);
        reviews.setProduct(product);
        reviews.setDate(LocalDate.now());
        reviewRepository.save(reviews);
        return "Review sent successfully";
    }

    @Override
    public String giveApplicationReview(ApplicationReviewDTO applicationReviewDTO) {

        User user = getLoggedInUser();
        Reviews reviews = new Reviews();
        reviews.setReviewType(ReviewType.APPLICATION_REVIEW);
        reviews.setFeedback(applicationReviewDTO.getFeedback());
        reviews.setRating(applicationReviewDTO.getRating());
        reviews.setUser(user);
        reviewRepository.save(reviews);

        return "Review sent successfully";
    }

    @Override
    public ReviewResponseDTO getAllReviews(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Reviews> reviews = reviewRepository.findAll(pageable);
        return getReviewResponse(reviews);
    }

    @Override
    public ReviewResponseDTO getAllProductReviews(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Reviews> filteredProducts = reviewRepository.findAllByReviewType(pageable, ReviewType.PRODUCT_REVIEW);
        return getReviewResponse(filteredProducts);
    }

    @Override
    public ReviewResponseDTO getAllReviewForAParticularProduct(int pageNo, int pageSize, String sortBy, String sortDir, Long productId) {
        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        Page<Reviews> filteredProducts = reviewRepository.findAllByProduct(pageable, product);
        return getReviewResponse(filteredProducts);
    }

    @Override
    public ReviewResponseDTO getAllApplicationReviews(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Reviews> filteredProducts = reviewRepository.findAllByReviewType(pageable, ReviewType.APPLICATION_REVIEW);
        return getReviewResponse(filteredProducts);
    }

    private ReviewResponseDTO getReviewResponse(Page<Reviews> reviews){
        List<Reviews> orderList = reviews.getContent();
        List<ReviewDTO> content = orderList.stream().map(review -> mapper.map(review, ReviewDTO.class)).collect(Collectors.toList());
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setContent(content);
        reviewResponseDTO.setPageNo(reviews.getNumber());
        reviewResponseDTO.setPageSize(reviews.getSize());
        reviewResponseDTO.setPageElement(reviews.getNumberOfElements());
        reviewResponseDTO.setTotalPages(reviews.getTotalPages());
        reviewResponseDTO.setLast(reviews.isLast());

        return reviewResponseDTO;

    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
