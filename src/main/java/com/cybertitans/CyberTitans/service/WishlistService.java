package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.model.Wishlist;

import java.util.List;


public interface WishlistService {
    Wishlist createWishlist(Wishlist wishlist);
    List<Wishlist> getAllWishlists(int pageNo,int pageSize, String sortBy, String sortDir);
    Wishlist getWishlistById(Long id);
    Wishlist updateWishlist(Long id, Wishlist wishlist);
    Wishlist deleteWishlist(Long id);

}
