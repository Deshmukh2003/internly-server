package com.internly.service;

import com.internly.entity.PasswordResetToken;
import com.internly.repository.*;
import java.security.SecureRandom;
import java.time.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {

  private static final int MAX_ATTEMPTS = 5;
  private static final Duration TTL = Duration.ofMinutes(10);
  private static final Duration COOLDOWN = Duration.ofSeconds(60);
  private final UserRepository users;
  private final PasswordResetTokenRepository tokens;
  private final PasswordEncoder encoder;
  private final EmailService email;
  private final SecureRandom random = new SecureRandom();

  public PasswordResetService(
    UserRepository users,
    PasswordResetTokenRepository tokens,
    PasswordEncoder encoder,
    EmailService email
  ) {
    this.users = users;
    this.tokens = tokens;
    this.encoder = encoder;
    this.email = email;
  }

  @Transactional
  public void request(String rawEmail) {
    String emailAddress = rawEmail.trim().toLowerCase();
    var user = users.findByEmailIgnoreCase(emailAddress);
    if (user.isEmpty()) return;
    var previous =
      tokens.findTopByEmailIgnoreCaseAndConsumedFalseOrderByCreatedAtDesc(
        emailAddress
      );
    if (
      previous.isPresent() &&
      previous.get().getCreatedAt().plus(COOLDOWN).isAfter(Instant.now())
    ) return;
    tokens.deleteByEmailIgnoreCaseAndConsumedFalse(emailAddress);
    String code = "%06d".formatted(random.nextInt(1_000_000));
    tokens.save(
      PasswordResetToken.builder()
        .email(emailAddress)
        .codeHash(encoder.encode(code))
        .createdAt(Instant.now())
        .expiresAt(Instant.now().plus(TTL))
        .attempts(0)
        .consumed(false)
        .build()
    );
    email.sendPasswordResetCode(emailAddress, code);
  }

  @Transactional
  public void reset(String rawEmail, String code, String newPassword) {
    String emailAddress = rawEmail.trim().toLowerCase();
    PasswordResetToken token = tokens
      .findTopByEmailIgnoreCaseAndConsumedFalseOrderByCreatedAtDesc(
        emailAddress
      )
      .orElseThrow(() ->
        new IllegalArgumentException("Invalid or expired reset code")
      );
    if (
      token.expired() || token.getAttempts() >= MAX_ATTEMPTS
    ) throw new IllegalArgumentException("Invalid or expired reset code");
    token.setAttempts(token.getAttempts() + 1);
    if (!encoder.matches(code, token.getCodeHash())) {
      tokens.save(token);
      throw new IllegalArgumentException("Invalid or expired reset code");
    }
    var user = users
      .findByEmailIgnoreCase(emailAddress)
      .orElseThrow(() ->
        new IllegalArgumentException("Invalid or expired reset code")
      );
    user.setPasswordHash(encoder.encode(newPassword));
    users.save(user);
    token.setConsumed(true);
    tokens.save(token);
  }
}
