package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.ProductImage;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    String createProduct(ProductDTO productDTO);
    ProductImage saveProductImage(String imgUrl, Long id);
    String updateProduct(ProductDTO productDTO, Long id);
    ProductImage updateProductImage(String ImgUrl, Long id);
    String deleteProduct(Long Id);
    AllOrderResponseDTO viewAllOrders(int pageNo, int pageSize, String sortBy, String sortDir);
    OrderResponseDTO viewParticularOrder(long orderId);
    String addAdmin(AdminDTO adminDTO);
    String editAdmin(UpdateProfileDTO updateProfileDTO, Long adminId);
    String deleteAdmin(Long adminId);
    UpdateProfileDTO updateSuperAdminDetails(UpdateProfileDTO adminDTO);

}
