package com.internly.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(
  name = "password_reset_tokens",
  indexes = @Index(name = "idx_password_reset_email", columnList = "email")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 254)
  private String email;

  @Column(nullable = false)
  private String codeHash;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private int attempts;

  @Column(nullable = false)
  private boolean consumed;

  public boolean expired() {
    return Instant.now().isAfter(expiresAt);
  }
}
