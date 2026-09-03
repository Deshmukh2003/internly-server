package com.internly.service;

import com.internly.dto.AuthDtos.*;
import com.internly.entity.User;
import com.internly.repository.UserRepository;
import com.internly.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;
  private final OtpService otpService;

  public AuthService(
    UserRepository users,
    PasswordEncoder encoder,
    JwtService jwt,
    OtpService otpService
  ) {
    this.users = users;
    this.encoder = encoder;
    this.jwt = jwt;
    this.otpService = otpService;
  }

  @Transactional
  public UserResponse register(RegisterRequest request) {
    String email = request.email().trim().toLowerCase();
    var existing = users.findByEmailIgnoreCase(email);
    if (existing.isPresent()) {
      User user = existing.get();
      if (
        !user.isVerified() && user.getRole() == User.Role.STUDENT
      ) return toResponse(user);
      throw new IllegalArgumentException(
        "An account with this email already exists"
      );
    }
    User user = users.save(
      User.builder()
        .email(email)
        .passwordHash(encoder.encode(request.password()))
        .role(User.Role.STUDENT)
        .verified(false)
        .build()
    );
    otpService.issue(email);
    return toResponse(user);
  }

  @Transactional
  public void verifyOtp(VerifyOtpRequest request) {
    otpService.verify(request.email(), request.code());
    User user = users
      .findByEmailIgnoreCase(request.email().trim())
      .orElseThrow();
    user.setVerified(true);
    users.save(user);
  }

  public void resendOtp(ResendOtpRequest request) {
    if (
      users
        .findByEmailIgnoreCase(request.email().trim())
        .filter(u -> !u.isVerified())
        .isEmpty()
    ) throw new IllegalArgumentException(
      "No pending verification exists for this email"
    );
    otpService.issue(request.email());
  }

  public LoginResponse login(LoginRequest request) {
    User user = users
      .findByEmailIgnoreCase(request.email().trim())
      .orElseThrow(() ->
        new IllegalArgumentException("Invalid email or password")
      );
    if (
      !encoder.matches(request.password(), user.getPasswordHash())
    ) throw new IllegalArgumentException("Invalid email or password");
    if (
      user.getRole() == User.Role.STUDENT && !user.isVerified()
    ) throw new IllegalArgumentException(
      "Please verify your email before signing in"
    );
    return new LoginResponse(
      jwt.generate(user.getEmail(), user.getRole().name()),
      "Bearer",
      user.getId(),
      user.getEmail(),
      user.getRole().name()
    );
  }

  public UserResponse me(String email) {
    return users
      .findByEmailIgnoreCase(email)
      .map(this::toResponse)
      .orElseThrow();
  }

  private UserResponse toResponse(User u) {
    return new UserResponse(
      u.getId(),
      u.getEmail(),
      u.getRole().name(),
      u.isVerified()
    );
  }
}
