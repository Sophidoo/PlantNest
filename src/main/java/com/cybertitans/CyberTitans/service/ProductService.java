package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.dto.ProductDTO;
import com.cybertitans.CyberTitans.dto.ProductResponseDTO;
import com.cybertitans.CyberTitans.enums.ProductType;

public interface ProductService {
    ProductDTO getProductById(Long id);
    ProductResponseDTO getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir, String filterBy, String filterParam, String startRange, String endRange);
    int getAvailableProductsCountByType();
    int getSoldProductsCountByType(ProductType productType);
    String processPaymentAndUpdateProductQuantities(OrderResponseDTO orders);
}
