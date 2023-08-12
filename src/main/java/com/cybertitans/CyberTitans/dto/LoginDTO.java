package com.cybertitans.CyberTitans.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDTO {
    @NotNull
    @NotEmpty(message = "Please input your email or username")
    private String usernameOrEmail;

    @NotNull
    @NotEmpty(message = "Please input your password")
    private String password;
}
