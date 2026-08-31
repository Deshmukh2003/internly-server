package com.internly.entity;

import jakarta.persistence.*; import lombok.*; import java.time.Instant;

@Entity @Table(name="email_otps", indexes=@Index(name="idx_email_otp_email", columnList="email"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmailOtp {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=254) private String email;
    @Column(nullable=false) private String codeHash;
    @Column(nullable=false) private Instant expiresAt;
    @Column(nullable=false) private Instant createdAt;
    @Column(nullable=false) private int attempts;
    @Column(nullable=false) private boolean consumed;
    public boolean expired() { return Instant.now().isAfter(expiresAt); }
}
