package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
}
