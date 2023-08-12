package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.enums.OtpType;
import com.cybertitans.CyberTitans.model.User;


public interface OtpService {

    String create(User user);
    String create(User user,OtpType otpType);
    Boolean verify(User user, String token, OtpType otpType);
}
