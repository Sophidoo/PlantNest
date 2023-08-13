package com.cybertitans.CyberTitans.model;

import com.cybertitans.CyberTitans.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;
    private String growthHabit;
    private String lightLevel;
    private String waterRequirement;
    private String image;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @NotNull
    @Column(nullable = false, columnDefinition = "int default 0")
    private int quantity;


    @Column(nullable = false, columnDefinition = "int default 0")
    @NotNull()
    private int quantitySold;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date createdAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlist;

    private double productPrice;

    private String category;

//    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reviews> reviews;

}
