package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.ShippingAddress;
import com.cybertitans.CyberTitans.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    Page<ShippingAddress> findAllByUser(User user, Pageable pageable);
    List<ShippingAddress> findAllByUser(User user);
    Optional<ShippingAddress> findFirstByUserAndIsDefaultShippingAddressTrue(User user);
    Boolean existsAddressByUserAndStreetAddressAndStateAndCity(User user, String streetAddress, String state, String city);
    Optional<ShippingAddress> findAddressByUserAndStreetAddressAndStateAndCity(User user, String streetAddress, String state, String city);
}
