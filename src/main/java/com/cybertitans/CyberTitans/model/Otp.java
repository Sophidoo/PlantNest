package com.cybertitans.CyberTitans.model;

import com.cybertitans.CyberTitans.enums.OtpType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "otps")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private OtpType otpType=OtpType.REGISTER;
}
