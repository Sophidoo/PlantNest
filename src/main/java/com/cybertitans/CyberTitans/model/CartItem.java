package com.cybertitans.CyberTitans.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    @Column(name = "quantity")
    private @NotNull int quantity;

    private double unitPrice;

    @Column(name = "price")
    private @NotNull double subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_Id", referencedColumnName = "id")
    private Cart cart;
}
