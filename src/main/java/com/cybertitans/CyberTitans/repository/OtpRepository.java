package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.enums.OtpType;
import com.cybertitans.CyberTitans.model.Otp;
import com.cybertitans.CyberTitans.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository  extends JpaRepository<Otp, Long> {
    void deleteAllByUser(User user);

    void deleteAllByUserAndOtpType(User user,OtpType otpType);
    Optional<Otp> findByUserAndTokenAndOtpType(User user, String token, OtpType otpType);
}
