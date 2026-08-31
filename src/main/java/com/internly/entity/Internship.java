package com.internly.entity;

import jakarta.persistence.*; import lombok.*; import java.time.*; import java.util.*;

@Entity @Table(name="internships", indexes={@Index(name="idx_internship_domain", columnList="domain"), @Index(name="idx_internship_deadline", columnList="applicationDeadline")}) @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Internship {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false) private String title; @Column(nullable=false, length=2000) private String description;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) private Company company; @Column(nullable=false) private String domain; private String qualification; private String eligibleBranches; private String location; private String workMode; private Integer durationWeeks; private Integer stipend; private LocalDate applicationDeadline; @Enumerated(EnumType.STRING) private Status status;
    @ManyToMany(fetch=FetchType.LAZY) @JoinTable(name="internship_skills", joinColumns=@JoinColumn(name="internship_id"), inverseJoinColumns=@JoinColumn(name="skill_id")) @Builder.Default private Set<Skill> requiredSkills = new HashSet<>();
    public enum Status { DRAFT, ACTIVE, CLOSED, EXPIRED, INACTIVE }
}
