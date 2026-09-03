package com.internly.entity;

import jakarta.persistence.*;
import java.time.*;
import java.util.*;
import lombok.*;

@Entity
@Table(
  name = "internships",
  indexes = {
    @Index(name = "idx_internship_domain", columnList = "domain"),
    @Index(
      name = "idx_internship_deadline",
      columnList = "applicationDeadline"
    ),
  }
)
@org.hibernate.annotations.Check(
  constraints = "(qualification is null or qualification in ('B.E.','B.Tech','B.Sc.','BCA','BBA','B.Arch')) and (work_mode is null or work_mode in ('On-site','Remote','Hybrid'))"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Internship {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 2000)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Company company;

  @Column(nullable = false)
  private String domain;

  private String qualification;

  @ElementCollection
  @CollectionTable(
    name = "internship_eligible_branches",
    joinColumns = @JoinColumn(name = "internship_id")
  )
  @Column(name = "branch", nullable = false)
  @Builder.Default
  private Set<String> eligibleBranches = new HashSet<>();

  private String location;
  private String workMode;
  private Integer durationWeeks;
  private Integer stipend;
  private LocalDate applicationDeadline;

  @Enumerated(EnumType.STRING)
  private Status status;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "internship_skills",
    joinColumns = @JoinColumn(name = "internship_id"),
    inverseJoinColumns = @JoinColumn(name = "skill_id")
  )
  @Builder.Default
  private Set<Skill> requiredSkills = new HashSet<>();

  public enum Status {
    DRAFT,
    ACTIVE,
    CLOSED,
    EXPIRED,
    INACTIVE,
  }
}
