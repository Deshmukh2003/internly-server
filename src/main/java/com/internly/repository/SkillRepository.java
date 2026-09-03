package com.internly.repository;

import com.internly.entity.Skill;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
  Optional<Skill> findByNormalizedName(String normalizedName);
}
