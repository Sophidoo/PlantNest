package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.Orders;
import com.cybertitans.CyberTitans.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findAllByUser(Pageable pageable, User user);
}
