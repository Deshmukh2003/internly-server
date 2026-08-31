package com.internly.dto;

import jakarta.validation.constraints.*;

public final class AuthDtos {
    private AuthDtos() {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank @Size(min = 8, max = 128) String password) {}
    public record RegisterRequest(@NotBlank @Email String email, @NotBlank @Size(min = 8, max = 128) String password) {}
    public record LoginResponse(String token, String tokenType, Long userId, String email, String role) {}
    public record UserResponse(Long id, String email, String role, boolean verified) {}
}
