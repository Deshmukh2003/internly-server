package com.internly.service;

import com.internly.dto.AuthDtos.*; import com.internly.entity.User; import com.internly.repository.UserRepository; import com.internly.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) { this.users=users; this.encoder=encoder; this.jwt=jwt; }
    @Transactional public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) throw new IllegalArgumentException("An account with this email already exists");
        User user = users.save(User.builder().email(email).passwordHash(encoder.encode(request.password())).role(User.Role.STUDENT).verified(false).build());
        return toResponse(user);
    }
    public LoginResponse login(LoginRequest request) {
        User user = users.findByEmailIgnoreCase(request.email().trim()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!encoder.matches(request.password(), user.getPasswordHash())) throw new IllegalArgumentException("Invalid email or password");
        if (user.getRole() == User.Role.STUDENT && !user.isVerified()) throw new IllegalArgumentException("Please verify your email before signing in");
        return new LoginResponse(jwt.generate(user.getEmail(), user.getRole().name()), "Bearer", user.getId(), user.getEmail(), user.getRole().name());
    }
    public UserResponse me(String email) { return users.findByEmailIgnoreCase(email).map(this::toResponse).orElseThrow(); }
    private UserResponse toResponse(User u) { return new UserResponse(u.getId(), u.getEmail(), u.getRole().name(), u.isVerified()); }
}
