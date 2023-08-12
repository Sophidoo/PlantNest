package com.cybertitans.CyberTitans.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String firstname;
    private String lastname;

    @NotNull
    @NotEmpty(message = "Username can not be empty")
    @Size(min = 4, message = "Username should be more than 4 characters")
    private String username;

    @NotNull
    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Email address should not be empty", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Please provide a valid email address", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @Size(min = 6, max = 20)
    private String phoneNumber;


}
