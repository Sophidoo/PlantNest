package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.AllOrderResponseDTO;
import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.dto.OrderStatusDTO;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    AllOrderResponseDTO viewUserOrderHistory (int pageNo, int pageSize, String sortBy, String sortDir);
    OrderResponseDTO updateOrderStatus (Long orderId, OrderStatusDTO orderStatusDTO);
    OrderResponseDTO viewParticularOrder(Long orderId);
    OrderResponseDTO cancelOrder(Long orderId);

    AllOrderResponseDTO viewOrderHistory(int pageNo, int pageSize, String sortBy, String sortDir);
}
