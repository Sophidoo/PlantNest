package com.cybertitans.CyberTitans.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartDTO {
    private Long cartId;
    private int quantity;
    private double cartTotal;
    private List<CartItemDTO> cartItemList;
    private Long userId;
}
