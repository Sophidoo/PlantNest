package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.ShippingAddressDTO;
import com.cybertitans.CyberTitans.dto.UserAddressResponseDTO;
import com.cybertitans.CyberTitans.model.ShippingAddress;
import com.cybertitans.CyberTitans.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ShippingAddressService {
    ShippingAddressDTO saveAddress(ShippingAddressDTO shippingAddressDTO);
    UserAddressResponseDTO getAllUserAddress(int pageNo, int pageSize, String sortBy, String sortDir);
    ShippingAddressDTO updateAddress(Long addressId, ShippingAddressDTO shippingAddressDTO);
    String deleteAddress(Long addressId);
    ShippingAddressDTO getUserDefaultAddress();
}
