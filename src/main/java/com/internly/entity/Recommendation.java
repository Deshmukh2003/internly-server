package com.internly.entity;

import jakarta.persistence.*; import lombok.*; import java.time.Instant;

@Entity @Table(name="recommendations", uniqueConstraints=@UniqueConstraint(name="uk_recommendation_student_internship", columnNames={"student_id","internship_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recommendation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="student_id") private User student;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="internship_id") private Internship internship;
    @Column(nullable=false) private int matchScore;
    @Column(nullable=false) private long matchedSkills;
    @Column(nullable=false) private int requiredSkills;
    @Column(nullable=false) private boolean domainMatched;
    @Column(nullable=false) private boolean qualificationMatched;
    @Column(nullable=false, length=500) private String explanation;
    @Column(nullable=false) private Instant generatedAt;
}
