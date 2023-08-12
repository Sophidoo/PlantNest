package com.cybertitans.CyberTitans.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( columnDefinition = "int default 0")
    private double cartTotal;

    @Column(columnDefinition = "int default 0")
    private int cartQuantity;

    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<CartItem> cartItemList;


    @JsonIgnore
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    public Cart(User user) {
        this.user = user;
    }
}
