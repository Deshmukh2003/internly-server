package com.internly.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(
  name = "users",
  uniqueConstraints = @UniqueConstraint(
    name = "uk_users_email",
    columnNames = "email"
  )
)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 254)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role;

  @Column(nullable = false)
  private boolean verified;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void onCreate() {
    createdAt = Instant.now();
  }

  public enum Role {
    STUDENT,
    ADMIN,
  }
}
