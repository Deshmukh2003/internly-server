package com.internly.repository;

import com.internly.entity.EmailOtp; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findTopByEmailIgnoreCaseAndConsumedFalseOrderByCreatedAtDesc(String email);
    void deleteByEmailIgnoreCaseAndConsumedFalse(String email);
}
