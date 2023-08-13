package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.model.UserOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    Page<UserOrder> findAllByUser(Pageable pageable, User user);
}
