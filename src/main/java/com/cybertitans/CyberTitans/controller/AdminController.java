package com.cybertitans.CyberTitans.controller;


import com.cybertitans.CyberTitans.dto.AllOrderResponseDTO;
import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.dto.OrderStatusDTO;
import com.cybertitans.CyberTitans.dto.ProductDTO;
import com.cybertitans.CyberTitans.enums.ProductType;
import com.cybertitans.CyberTitans.model.ProductImage;
import com.cybertitans.CyberTitans.service.*;
import com.cybertitans.CyberTitans.utls.AppConstant;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin")
@Tag(
        name = " ADMIN SERVICES REST API",
        description = "These endpoints are the services the admin can render"
)
public class AdminController {
    private final AdminService adminService;
    private final CloudinaryService cloudinaryService;
    private final OrderService orderService;
    private final ProductService productService;

    public AdminController(AdminService adminService, CloudinaryService cloudinaryService, OrderService orderService, ProductService productService, AuditTrialService auditTrialService) {
        this.adminService = adminService;
        this.cloudinaryService = cloudinaryService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/create-product")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO){
        String product = adminService.createProduct(productDTO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/image-upload/{id}")
    public ResponseEntity<ProductImage> imageUpload(@RequestPart MultipartFile image, @PathVariable("id") Long id){
        ProductImage productImage = adminService.saveProductImage(cloudinaryService.uploadFIle(image), id);
        return new ResponseEntity<>(productImage, HttpStatus.CREATED);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("/updateImage/{id}")
    public ResponseEntity<ProductImage> updateProductImage(@RequestPart MultipartFile image, @PathVariable Long id){
        ProductImage productImage = adminService.updateProductImage(cloudinaryService.uploadFIle(image), id);
        return new ResponseEntity<>(productImage, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id){
        String prd = adminService.updateProduct(productDTO, id);
        return new ResponseEntity<>(prd, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(value = "id") Long id){
        String prd = adminService.deleteProduct(id);
        return new ResponseEntity<>(prd, HttpStatus.OK);
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
    @PutMapping("update-order-status/{orderId}/")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus (@PathVariable("orderId") Long orderId, @RequestBody OrderStatusDTO orderStatusDTO){
        OrderResponseDTO orderResponseDTO = orderService.updateOrderStatus(orderId, orderStatusDTO);
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/orders")
    public ResponseEntity<AllOrderResponseDTO> viewAllOrders (
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY_ORDER, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        AllOrderResponseDTO allOrderResponseDTO = adminService.viewAllOrders(pageNo, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allOrderResponseDTO, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/products-available-count")
    public ResponseEntity<Integer> getAvailableProductsCountByType() {
        int availableProductsCountByType = productService.getAvailableProductsCountByType();
        return new ResponseEntity<>(availableProductsCountByType, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/products-sold-count/{productType}")
    public ResponseEntity<Integer> getSoldProductsCountByType(@PathVariable ProductType productType) {
        int soldProductsCountByType = productService.getSoldProductsCountByType(productType);
        return new ResponseEntity<>(soldProductsCountByType, HttpStatus.OK);
    }

}
