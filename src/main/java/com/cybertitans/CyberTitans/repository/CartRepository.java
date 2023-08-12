package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.Cart;
import com.cybertitans.CyberTitans.model.CartItem;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
