package com.internly.repository;

import com.internly.entity.Skill; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> { Optional<Skill> findByNormalizedName(String normalizedName); }
