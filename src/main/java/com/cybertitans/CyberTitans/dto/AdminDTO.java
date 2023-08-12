package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    @Size(min = 6)
    private String password;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
