package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.dto.ShippingAddressDTO;

public interface CheckoutService {
    OrderResponseDTO checkout(ShippingAddressDTO shippingAddressDTO);
}
