package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.OrderStatus;
import com.cybertitans.CyberTitans.model.OrderItems;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.ShippingAddress;
import com.cybertitans.CyberTitans.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private Date dateOrdered;
    private Double subTotal;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private ShippingAddress shippingAddress;

    private List<Product> productList;


}
