package com.cybertitans.CyberTitans.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDTO {
    @NotNull
    @NotEmpty(message = "Please input your old password")
    private String oldPassword;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 40, message = "password must be up to 6 characters")
    private String newPassword;

    @NotNull
    @NotEmpty(message = "Please confirm your password")
    private String confirmPassword;
}
