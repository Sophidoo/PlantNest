package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.Cart;
import com.cybertitans.CyberTitans.model.CartItem;
import com.cybertitans.CyberTitans.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteAllByCart_Id(long id);
    Optional<CartItem> findByCart_IdAndId(long cartId, long cartItemId);
    List<CartItem> findCartItemByCart_Id(long cartId);
}
