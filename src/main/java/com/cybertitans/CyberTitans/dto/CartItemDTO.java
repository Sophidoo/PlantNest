package com.cybertitans.CyberTitans.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private long id;
    private long productId;
    private String productName;
    private String productImage;
    private String productSize;
    private String description;
    private int quantity;
    private double unitPrice;
    private double subTotal;
    private long cartId;
}

