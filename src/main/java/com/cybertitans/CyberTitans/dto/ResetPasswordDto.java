package com.cybertitans.CyberTitans.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Optional;

@Data
public class ResetPasswordDto {
    @NotNull
    @NotEmpty(message = "Please input your email or username")
    private String usernameOrEmail;
    private String password;
    private String otp;

      public Optional<String> getOtp() {
        return Optional.ofNullable(otp);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }
}
