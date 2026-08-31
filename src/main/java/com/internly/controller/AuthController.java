package com.internly.controller;

import com.internly.dto.AuthDtos.*; import com.internly.service.AuthService; import jakarta.validation.Valid; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth; public AuthController(AuthService auth) { this.auth=auth; }
    @PostMapping("/register") public UserResponse register(@Valid @RequestBody RegisterRequest request) { return auth.register(request); }
    @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest request) { return auth.login(request); }
    @PostMapping("/verify-otp") public void verifyOtp(@Valid @RequestBody VerifyOtpRequest request) { auth.verifyOtp(request); }
    @PostMapping("/resend-otp") public void resendOtp(@Valid @RequestBody ResendOtpRequest request) { auth.resendOtp(request); }
    @GetMapping("/me") public UserResponse me(Authentication authentication) { return auth.me(authentication.getName()); }
}
