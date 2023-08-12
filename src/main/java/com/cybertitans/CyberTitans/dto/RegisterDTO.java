package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.enums.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotNull
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 4, message = "Username should be up to 4 characters")
    private String username;


    @NotNull
    @NotEmpty
    @Size(min = 4,  message = "Email should be up to 4 characters")
    @Email(message = "Email address should not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Please provide a valid email address", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 40, message = "password must be up to 6 characters")
    private String password;

}
