package com.internly.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(
  name = "notifications",
  indexes = @Index(
    name = "idx_notification_student_read",
    columnList = "student_id,read_at"
  )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id")
  private User student;

  @Column(nullable = false, length = 160)
  private String title;

  @Column(nullable = false, length = 500)
  private String message;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "read_at")
  private Instant readAt;

  @PrePersist
  void onCreate() {
    createdAt = Instant.now();
  }
}
