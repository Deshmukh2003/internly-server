package com.internly.controller;

import com.internly.dto.AuthDtos.*; import com.internly.service.AuthService; import jakarta.validation.Valid; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth; private final com.internly.service.PasswordResetService passwordReset;
    public AuthController(AuthService auth, com.internly.service.PasswordResetService passwordReset) { this.auth=auth; this.passwordReset=passwordReset; }
    @PostMapping("/register") public UserResponse register(@Valid @RequestBody RegisterRequest request) { return auth.register(request); }
    @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest request) { return auth.login(request); }
    @PostMapping("/verify-otp") public void verifyOtp(@Valid @RequestBody VerifyOtpRequest request) { auth.verifyOtp(request); }
    @PostMapping("/resend-otp") public void resendOtp(@Valid @RequestBody ResendOtpRequest request) { auth.resendOtp(request); }
    @PostMapping("/forgot-password") public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) { passwordReset.request(request.email()); }
    @PostMapping("/reset-password") public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) { passwordReset.reset(request.email(), request.code(), request.newPassword()); }
    @GetMapping("/me") public UserResponse me(Authentication authentication) { return auth.me(authentication.getName()); }
}
