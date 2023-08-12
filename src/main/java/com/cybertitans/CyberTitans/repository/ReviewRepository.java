package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.enums.ProductType;
import com.cybertitans.CyberTitans.enums.ReviewType;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.Reviews;
import com.cybertitans.CyberTitans.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    Page<Reviews> findAllByReviewType (Pageable pageable, ReviewType reviewType);
    Page<Reviews> findAllByProduct(Pageable pageable, Product product);
}
