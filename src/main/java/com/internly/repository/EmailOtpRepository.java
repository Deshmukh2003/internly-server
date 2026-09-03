package com.internly.repository;

import com.internly.entity.EmailOtp;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
  Optional<
    EmailOtp
  > findTopByEmailIgnoreCaseAndConsumedFalseOrderByCreatedAtDesc(String email);
  void deleteByEmailIgnoreCaseAndConsumedFalse(String email);
}
