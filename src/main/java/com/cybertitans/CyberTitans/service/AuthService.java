package com.cybertitans.CyberTitans.service;

import com.cybertitans.CyberTitans.dto.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    String Login(LoginDTO loginDTO);
    String Register(RegisterDTO registerDTO);
    String updateUserdetails(UpdateProfileDTO updateProfileDTO);
    String updatePassword(UpdatePasswordDTO updatePasswordDTO);
    UserDetailsDTO getUserDetails();

    String RegisterWithGoogle(OAuth2User oAuth2User);

    String resetPassword(ResetPasswordDto resetPasswordDto);

    String verifyEmail(VerifyEmailDto verifyEmailDto);

    String sendVerificationToken();
}
