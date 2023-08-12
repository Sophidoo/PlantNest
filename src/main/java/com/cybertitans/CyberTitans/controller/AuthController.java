package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = " USER AUTHENTICATION REST API"
)
public class AuthController {
    private AuthService authService;


    @Value("${app.frontend-url}")
    private String frontedUrl;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    @Operation(
            summary = "This Endpoint is to register a user" ,
            description = "It will register a new user"
    )
    @PostMapping(value = {"/register"})
    public ResponseEntity<String> regiserUser(@Valid @RequestBody RegisterDTO registerDTO){
        String register = authService.Register(registerDTO);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@Valid @RequestBody LoginDTO loginDTO){
        String login = authService.Login(loginDTO);
        return  new ResponseEntity<>(login, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("/update")
    public ResponseEntity<JwtResponseDTO> updateuser(@Valid @RequestBody UpdateProfileDTO updateProfileDTO){
        String token = authService.updateUserdetails(updateProfileDTO);
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setAccessToken(token);
        return new ResponseEntity<>(jwtResponseDTO, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping()
    public ResponseEntity<UserDetailsDTO> getUserDetails(){
        UserDetailsDTO user = authService.getUserDetails();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO){
        String password = authService.updatePassword(updatePasswordDTO);
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    @GetMapping("/oauth2")
    public ResponseEntity<?> noAuth() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/oauthsuccess")
    public void getPrincipal(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse httpServletResponse) {
       String token=  authService.RegisterWithGoogle(oAuth2User);
       String redirect  = frontedUrl+"?token="+token;
        httpServletResponse.setHeader("Location",redirect);
        httpServletResponse.setStatus(302);
    }

    @PostMapping("/reset")
    public String resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto){
        return authService.resetPassword(resetPasswordDto);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/verify")
    public String verifyEmail(@Valid @RequestBody VerifyEmailDto verifyEmailDto){
        return authService.verifyEmail(verifyEmailDto);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/resend")
    public String resendVerificationEmail(){
        return authService.sendVerificationToken();
    }
}
