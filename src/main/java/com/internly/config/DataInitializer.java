package com.internly.config;

import com.internly.entity.User; import com.internly.repository.UserRepository; import org.springframework.beans.factory.annotation.Value; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Configuration;

@Configuration
public class DataInitializer {
    @Bean CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder, @Value("${security.admin.email}") String email, @Value("${security.admin.password}") String password) {
        return args -> { if (users.countByRole(User.Role.ADMIN) == 0) users.save(User.builder().email(email.trim().toLowerCase()).passwordHash(encoder.encode(password)).role(User.Role.ADMIN).verified(true).build()); };
    }
}
