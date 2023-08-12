package com.cybertitans.CyberTitans.dto;

import com.cybertitans.CyberTitans.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressDTO {
    private Long user_id;
    private String streetAddress;
    private String city;
//    @Email
//    private String email;
    private String state;
    private String country;
    private boolean isDefaultShippingAddress;
}
