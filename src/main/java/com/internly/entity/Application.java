package com.internly.entity;

import jakarta.persistence.*; import lombok.*; import java.time.Instant;

@Entity @Table(name="applications", uniqueConstraints=@UniqueConstraint(name="uk_application_student_internship", columnNames={"student_id","internship_id"}), indexes=@Index(name="idx_application_status", columnList="status"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="student_id") private User student;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="internship_id") private Internship internship;
    @Column(length=2000) private String coverNote;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private Status status;
    @Column(nullable=false, updatable=false) private Instant appliedAt;
    @PrePersist void onCreate() { appliedAt = Instant.now(); if (status == null) status = Status.SUBMITTED; }
    public enum Status { SUBMITTED, SHORTLISTED, REJECTED, ACCEPTED, WITHDRAWN }
}
