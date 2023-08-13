package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.model.ShippingAddress;
import com.cybertitans.CyberTitans.service.OrderService;
import com.cybertitans.CyberTitans.service.ProductService;
import com.cybertitans.CyberTitans.service.ShippingAddressService;
import com.cybertitans.CyberTitans.utls.AppConstant;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user")
@Tag(
        name = " USER SERVICES REST API",
        description = "These endpoints are the services the user can render"
)
public class UserController {
    private final ProductService productService;
    private final OrderService orderService;
    private final ShippingAddressService shippingAddress;


    public UserController(ProductService productService, OrderService orderService, ShippingAddressService shippingAddress) {
        this.productService = productService;
        this.orderService = orderService;
        this.shippingAddress = shippingAddress;
    }


    @GetMapping("/getAllProducts")
    public ResponseEntity<ProductResponseDTO> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "1000", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "filterBy", defaultValue = AppConstant.DEFAULT_FILTER_BY_PARAMETER, required = false) String filterBy,
            @RequestParam(value = "filterParam", defaultValue = AppConstant.DEFAULT_FILTER_PARAMETER, required = false) String filterParam,
            @RequestParam(value = "productPriceStartRange", defaultValue = AppConstant.DEFAULT_PRODUCT_PRICE_START_RANGE, required = false) String productPriceStartRange,
            @RequestParam(value = "productPriceEndRange", defaultValue = AppConstant.DEFAULT_PRODUCT_PRICE_END_RANGE, required = false) String productPriceEndRange
    ) throws ServletException {
        ProductResponseDTO allProducts = productService.getAllProducts(pageNo, pageSize, sortBy, sortDir, filterBy, filterParam, productPriceStartRange, productPriceEndRange);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/getSingleProduct/{id}")
    public ResponseEntity<ProductDTO> fetchSingleProduct(@PathVariable Long id){
        ProductDTO productById = productService.getProductById(id);
        return new ResponseEntity<>(productById, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/orders")
    public ResponseEntity<AllOrderResponseDTO> viewOrderHistory (
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY_ORDER, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        AllOrderResponseDTO allOrderResponseDTO = orderService.viewOrderHistory(pageNo, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allOrderResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(
            @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        String orderResponse = orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.ok(orderResponse);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/view-single-order/{orderId}")
    public ResponseEntity<OrderResponseDTO> viewParticularOrder(@PathVariable Long orderId) {
        OrderResponseDTO orderResponseDTO = orderService.viewParticularOrder(orderId);
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/save-address")
    public ResponseEntity<ShippingAddressDTO> saveAddress(@RequestBody ShippingAddressDTO shippingAddressDTO){
        ShippingAddressDTO shippingAddressDTO1 = shippingAddress.saveAddress(shippingAddressDTO);
        return new ResponseEntity<>(shippingAddressDTO1, HttpStatus.CREATED);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/get-addresses")
    public ResponseEntity<UserAddressResponseDTO> getAllAddresses(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY_ORDER, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        UserAddressResponseDTO allUserAddress = shippingAddress.getAllUserAddress(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allUserAddress, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', SUPER_ADMIN')")
    @PatchMapping("/update-address/{id}")
    public ResponseEntity<ShippingAddressDTO> updateAddess(@PathVariable Long id, @RequestBody ShippingAddressDTO shippingAddressDTO){
        ShippingAddressDTO shippingAddressDTO1 = shippingAddress.updateAddress(id, shippingAddressDTO);
        return new ResponseEntity<>(shippingAddressDTO1, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', SUPER_ADMIN')")
    @DeleteMapping("/delete-address/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id){
        String shippingAddressDTO1 = shippingAddress.deleteAddress(id);
        return new ResponseEntity<>(shippingAddressDTO1, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', SUPER_ADMIN')")
    @GetMapping("/get-default-address")
    public ResponseEntity<ShippingAddressDTO> getDefaultAddress(){
        ShippingAddressDTO shippingAddressDTO1 = shippingAddress.getUserDefaultAddress();
        return new ResponseEntity<>(shippingAddressDTO1, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', SUPER_ADMIN')")
    @PatchMapping("/cancel-order/{orderId}")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long orderId){
        OrderResponseDTO orderResponseDTO = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
    }



}
