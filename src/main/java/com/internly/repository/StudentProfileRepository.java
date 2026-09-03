package com.internly.repository;

import com.internly.entity.StudentProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository
  extends JpaRepository<StudentProfile, Long>
{
  Optional<StudentProfile> findByUserId(Long userId);
}
