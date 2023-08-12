package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.CartDTO;
import com.cybertitans.CyberTitans.dto.CartItemDTO;
import com.cybertitans.CyberTitans.dto.ReviewResponseDTO;

public interface CartService {
    CartItemDTO addToCart(Long productId);
    String clearCart();
    String removeCartItem(Long cartItemId);
    String reduceQuantityInCart(Long cartItemid);
    String increaseQuantityInCart(Long cartItemId);
    CartDTO viewCart();
}
