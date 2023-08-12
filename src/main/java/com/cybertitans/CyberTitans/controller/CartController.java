package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.dto.CartDTO;
import com.cybertitans.CyberTitans.dto.CartItemDTO;
import com.cybertitans.CyberTitans.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("**")
@RestController
@RequestMapping("/api/v1/cart")
@Tag(
        name = " CART REST API",
        description = "This section involves anything that has to do with users and cart interaction"
)
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/add-to-cart/{productId}")
    public ResponseEntity<CartItemDTO> addToCart(@PathVariable("productId") long productId){
        CartItemDTO cartItemDTO = cartService.addToCart(productId);
        return new ResponseEntity<>(cartItemDTO, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("/clear-cart")
    public ResponseEntity<String> clearCart() {
        String clearcart = cartService.clearCart();
        return new ResponseEntity<>(clearcart, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("/reduce-item-quantity/{cart-item-id}")
    public ResponseEntity<String> reduceItemQuantity(@PathVariable("cart-item-id") long cartItemId) {
        String cart = cartService.reduceQuantityInCart(cartItemId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("/increase-item-quantity/{cart-item-id}")
    public ResponseEntity<String> increaseItemQuantity(@PathVariable("cart-item-id") long cartItemId) {
        String cart = cartService.increaseQuantityInCart(cartItemId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("/remove-cart-item/{cart-item-id}")
    public ResponseEntity<String> removeCartItem(@PathVariable("cart-item-id") long cartItemId) {
        String cart = cartService.removeCartItem(cartItemId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/view-cart")
    public ResponseEntity<CartDTO> viewCart(){
        CartDTO cartDTO = cartService.viewCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}
