package com.internly.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "skills",
  uniqueConstraints = @UniqueConstraint(
    name = "uk_skills_normalized_name",
    columnNames = "normalizedName"
  )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String normalizedName;
}
