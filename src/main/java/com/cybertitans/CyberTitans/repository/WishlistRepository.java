package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository  extends JpaRepository<Wishlist, Long> {
}
