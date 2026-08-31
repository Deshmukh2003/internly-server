package com.internly.entity;

import jakarta.persistence.*; import lombok.*; import java.util.*;

@Entity @Table(name="student_profiles") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentProfile {
    @Id private Long userId;
    @OneToOne(fetch=FetchType.LAZY, optional=false) @MapsId @JoinColumn(name="user_id") private User user;
    @Column(length=120) private String fullName; @Column(length=30) private String mobile; @Column(length=120) private String domain;
    @Column(length=120) private String qualification; @Column(length=160) private String college; private Integer graduationYear;
    @Column(length=1000) private String interests;
    @ManyToMany(fetch=FetchType.LAZY) @JoinTable(name="student_skills", joinColumns=@JoinColumn(name="student_id"), inverseJoinColumns=@JoinColumn(name="skill_id")) @Builder.Default private Set<Skill> skills = new HashSet<>();
}
