package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.dto.ShippingAddressDTO;
import com.cybertitans.CyberTitans.service.CheckoutService;
import com.cybertitans.CyberTitans.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/checkout")
@Tag(
        name = " CART REST API",
        description = "This section is for checkout and payment"
)
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final ProductService productService;

    public CheckoutController(CheckoutService checkoutService, ProductService productService) {
        this.checkoutService = checkoutService;
        this.productService = productService;
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping
    public ResponseEntity<OrderResponseDTO> checkout(@RequestBody ShippingAddressDTO shippingAddressDTO){
        OrderResponseDTO checkout = checkoutService.checkout(shippingAddressDTO);
        return new ResponseEntity<>(checkout, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/payment")
    public ResponseEntity<String> payment(@RequestBody OrderResponseDTO orderResponseDTO){
        String pay = productService.processPaymentAndUpdateProductQuantities(orderResponseDTO);
        return new ResponseEntity<>(pay, HttpStatus.OK);

    }

}
