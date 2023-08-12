package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
