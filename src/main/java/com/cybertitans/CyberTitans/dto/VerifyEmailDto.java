package com.cybertitans.CyberTitans.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyEmailDto {
    @NotNull
    @NotEmpty(message = "Please input your email or username")
    @Min(6)
    private String otp;
}
