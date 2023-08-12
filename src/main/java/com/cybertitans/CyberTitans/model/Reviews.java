package com.cybertitans.CyberTitans.model;

import com.cybertitans.CyberTitans.enums.ReviewType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedback;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;


    private double rating;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDate date = LocalDate.now();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;
}
