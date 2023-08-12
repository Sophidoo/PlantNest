package com.cybertitans.CyberTitans.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImageId;

    @NotNull(message = "imageURL cannot be empty")
    private String imageURL;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
