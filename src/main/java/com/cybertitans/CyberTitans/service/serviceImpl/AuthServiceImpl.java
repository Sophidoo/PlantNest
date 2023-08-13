package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.enums.OtpType;
import com.cybertitans.CyberTitans.enums.Provider;
import com.cybertitans.CyberTitans.enums.Roles;
import com.cybertitans.CyberTitans.exception.Exception;
import com.cybertitans.CyberTitans.model.Cart;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.CartRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.security.JwtTokenProvider;
import com.cybertitans.CyberTitans.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper mapper;
    private final CartRepository cartRepository;

    private final MailingServiceImpl mailingService;

    private final OtpServiceImpl otpService;


    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, ModelMapper mapper, MailingServiceImpl mailingService, OtpServiceImpl otpService, JwtTokenProvider jwtTokenProvider, CartRepository cartRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.mailingService = mailingService;
        this.otpService = otpService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cartRepository = cartRepository;
    }

    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public String Login(LoginDTO loginDTO) {
        if (!userRepository.existsByUsername(loginDTO.getUsernameOrEmail()) && !userRepository.existsByEmail(loginDTO.getUsernameOrEmail())) {
            throw  new Exception(HttpStatus.BAD_REQUEST, "User " + loginDTO.getUsernameOrEmail() + " does not exist, Please Register");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String Register(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new Exception(HttpStatus.BAD_REQUEST, "Username already exists, Login instead");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw  new Exception(HttpStatus.BAD_REQUEST, "Email already exists, Login instead");
        }
        if (registerDTO.getPassword().length() < 6) {
            throw new Exception(HttpStatus.BAD_REQUEST, "Password length must be more than 6 characters");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder().encode(registerDTO.getPassword()));

        user.setRole(Roles.USER);

        User save = userRepository.save(user);

//        Creating user cart as user is created
        Cart cart = new Cart(save);
        cart.setUser(save);
        cartRepository.save(cart);

        String otp = otpService.create(user);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("otpType", "Registration");
        parameters.put("token", otp);
        parameters.put("delay", "30 minutes");
        try {
            mailingService.sendMessageWithTemplate(user.getEmail(), "Verify your account", "verify", parameters);
        } catch (IOException err
        ) {
            System.out.println(err.getMessage());
        }
        return "Successful, Kindly login to continue";
    }

    @Override
    public String updateUserdetails(UpdateProfileDTO updateProfileDTO) {
        User loggedUser = getLoggedInUser();
        loggedUser.setUsername(updateProfileDTO.getUsername());
        loggedUser.setFirstname(updateProfileDTO.getFirstname());
        loggedUser.setLastname(updateProfileDTO.getLastname());
        loggedUser.setEmail(updateProfileDTO.getEmail());
        loggedUser.setPhoneNumber(updateProfileDTO.getPhoneNumber());
        userRepository.save(loggedUser);
        return "User details updates successfully";
    }

    @Override
    public String updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        User loggedUser = getLoggedInUser();
        boolean matchPasswordWithOldPassword = passwordEncoder().matches(updatePasswordDTO.getOldPassword(), loggedUser.getPassword());
        if (!matchPasswordWithOldPassword) {
            return new RuntimeException("The password is incorrect, Please try again").getMessage();
        }
        if (updatePasswordDTO.getNewPassword().length() < 6) {
            return new Exception(HttpStatus.BAD_REQUEST, "Password length must be more than 6 characters").getMessage();
        }
        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmPassword())) {
            return new RuntimeException(("Password does not match")).getMessage();
        }
        loggedUser.setPassword(updatePasswordDTO.getNewPassword());
        userRepository.save(loggedUser);

        return "Password saved successfully";
    }

    @Override
    public UserDetailsDTO getUserDetails() {
        User loggedUser = getLoggedInUser();
        loggedUser.getFirstname();
        loggedUser.getLastname();
        loggedUser.getEmail();
        loggedUser.getPassword();
        loggedUser.getUsername();
        return mapper.map(loggedUser, UserDetailsDTO.class);
    }

    @Override
    public String RegisterWithGoogle(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String firstname = (String) attributes.get("family_name");
        String lastname = (String) attributes.get("given_name");
        String email = (String) attributes.get("email");
        String username = email.substring(0, email.indexOf("@"));
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setVerified(true);
        user.setProvider(Provider.GOOGLE);
        user.setRole(Roles.USER);
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(user);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto) {
        if (!userRepository.existsByUsername(resetPasswordDto.getUsernameOrEmail()) && !userRepository.existsByEmail(resetPasswordDto.getUsernameOrEmail())) {
            return new Exception(HttpStatus.BAD_REQUEST, "User " + resetPasswordDto.getUsernameOrEmail() + " does not exist, Please Register").getMessage();
        }
        Optional<User> user = userRepository.findByUsernameOrEmail(resetPasswordDto.getUsernameOrEmail(), resetPasswordDto.getUsernameOrEmail());
        if (resetPasswordDto.getOtp().isPresent() && resetPasswordDto.getPassword().isPresent() && user.isPresent()) {
            Boolean verified = otpService.verify(user.get(), resetPasswordDto.getOtp().get(), OtpType.RESET);
            if (verified) {
                user.get().setPassword(resetPasswordDto.getPassword().get());
                userRepository.save(user.get());
                otpService.clearOtps(user.get(), OtpType.RESET);
                return "Password reset Successfully Please login";
            }
            return "Invalid Token";
        }
        String otp = otpService.create(user.get(), OtpType.RESET);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("otpType", "Reset Password");
        parameters.put("token", otp);
        parameters.put("delay", "30 minutes");
        try {
            mailingService.sendMessageWithTemplate(user.get().getEmail(), "Verify your account", "verify", parameters);
            return "Mail Sent Please Verify";
        } catch (IOException exception
        ) {
            return "Error Sending Mail";
        }
    }

    @Override
    public String verifyEmail(VerifyEmailDto verifyEmailDto) {
        User user = getLoggedInUser();
        if (user.getVerified()) {
            return "User already verified";
        }
        Boolean verified = otpService.verify(user, verifyEmailDto.getOtp(), OtpType.REGISTER);
        if (verified) {
            user.setVerified(true);
            userRepository.save(user);
            otpService.clearOtps(user, OtpType.REGISTER);
            return "User Successfully verified";
        }
        return "Incorrect Token";
    }

    @Override
    public String sendVerificationToken() {
        User user = getLoggedInUser();
        if (user.getVerified()) {
            return "User already verified";
        }
        otpService.clearOtps(user, OtpType.REGISTER);
        String otp = otpService.create(user);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("otpType", "Registration");
        parameters.put("token", otp);
        parameters.put("delay", "30 minutes");
        try {
            mailingService.sendMessageWithTemplate(user.getEmail(), "Verify your account", "verify", parameters);
            return "Mail Sent Please Verify";
        } catch (IOException ignored
        ) {
            return "Error Sending Mail";
        }
    }

    private User getLoggedInUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
