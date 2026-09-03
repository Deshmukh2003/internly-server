package com.internly.repository;

import com.internly.entity.PasswordResetToken;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository
  extends JpaRepository<PasswordResetToken, Long>
{
  Optional<
    PasswordResetToken
  > findTopByEmailIgnoreCaseAndConsumedFalseOrderByCreatedAtDesc(String email);
  void deleteByEmailIgnoreCaseAndConsumedFalse(String email);
}
