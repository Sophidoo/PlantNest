package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.model.ShippingAddress;
import com.cybertitans.CyberTitans.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<ShippingAddress> findAllByShippingAddressesIsNotNull();
}
