package com.internly.service;

import com.internly.entity.EmailOtp;
import com.internly.repository.EmailOtpRepository;
import java.security.SecureRandom;
import java.time.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtpService {

  private static final int MAX_ATTEMPTS = 5;
  private static final Duration TTL = Duration.ofMinutes(10);
  private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60);
  private final EmailOtpRepository otps;
  private final PasswordEncoder encoder;
  private final EmailService email;
  private final SecureRandom random = new SecureRandom();

  public OtpService(
    EmailOtpRepository otps,
    PasswordEncoder encoder,
    EmailService email
  ) {
    this.otps = otps;
    this.encoder = encoder;
    this.email = email;
  }

  @Transactional
  public void issue(String emailAddress) {
    String emailAddressNormalized = emailAddress.trim().toLowerCase();
    var previous =
      otps.findTopByEmailIgnoreCaseAndConsumedFalseOrderByCreatedAtDesc(
        emailAddressNormalized
      );
    if (
      previous.isPresent() &&
      previous.get().getCreatedAt().plus(RESEND_COOLDOWN).isAfter(Instant.now())
    ) throw new IllegalArgumentException(
      "Please wait before requesting another code"
    );
    otps.deleteByEmailIgnoreCaseAndConsumedFalse(emailAddressNormalized);
    String code = "%06d".formatted(random.nextInt(1_000_000));
    otps.save(
      EmailOtp.builder()
        .email(emailAddressNormalized)
        .codeHash(encoder.encode(code))
        .createdAt(Instant.now())
        .expiresAt(Instant.now().plus(TTL))
        .attempts(0)
        .consumed(false)
        .build()
    );
    email.sendVerificationCode(emailAddressNormalized, code);
  }

  @Transactional
  public void verify(String emailAddress, String code) {
    EmailOtp otp = otps
      .findTopByEmailIgnoreCaseAndConsumedFalseOrderByCreatedAtDesc(
        emailAddress.trim()
      )
      .orElseThrow(() ->
        new IllegalArgumentException("Invalid or expired verification code")
      );
    if (
      otp.expired() || otp.getAttempts() >= MAX_ATTEMPTS
    ) throw new IllegalArgumentException(
      "Invalid or expired verification code"
    );
    otp.setAttempts(otp.getAttempts() + 1);
    if (!encoder.matches(code, otp.getCodeHash())) {
      otps.save(otp);
      throw new IllegalArgumentException(
        "Invalid or expired verification code"
      );
    }
    otp.setConsumed(true);
    otps.save(otp);
  }
}
