package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.model.Wishlist;
import com.cybertitans.CyberTitans.service.serviceImpl.WishlistServiceImpl;
import com.cybertitans.CyberTitans.utls.AppConstant;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("**")
@RestController
@RequestMapping("/api/v1/wishlist")
@Tag(
        name = " WISHLIST SERVICES REST API",
        description = "These endpoints conatin services for user interaction with wishlist"
)
public class WishlistController {
    private WishlistServiceImpl wishlistService;

    public WishlistController(WishlistServiceImpl wishlistService) {
        this.wishlistService = wishlistService;
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(@Valid @RequestBody Wishlist wishlist){
        return new ResponseEntity<>(wishlistService.createWishlist(wishlist), HttpStatus.CREATED);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable("id") long id){
        return ResponseEntity.ok(wishlistService.getWishlistById(id));
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> updateWishlist(@PathVariable("id") long id, @Valid @RequestBody Wishlist wishlist){
        Wishlist wishlist1 = wishlistService.updateWishlist(id,wishlist);
        return new ResponseEntity<>(wishlist1, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Wishlist> deleteWishlist(@PathVariable("id") long id){
        return ResponseEntity.ok(wishlistService.deleteWishlist(id));
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/wishlists")
    public ResponseEntity<List<Wishlist>> getAllWishlists(@RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDirection){
        return new ResponseEntity(wishlistService.getAllWishlists(pageNo,pageSize,sortBy,sortDirection), HttpStatus.OK);
    }
}
