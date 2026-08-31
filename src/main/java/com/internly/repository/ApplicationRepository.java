package com.internly.repository;

import com.internly.entity.Application; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByStudentIdAndInternshipId(Long studentId, Long internshipId);
    @Query("select a from Application a join fetch a.internship i join fetch i.company where a.student.id = :studentId order by a.appliedAt desc") Page<Application> findForStudent(@Param("studentId") Long studentId, Pageable pageable);
    @Query("select a from Application a join fetch a.student s join fetch a.internship i join fetch i.company order by a.appliedAt desc") Page<Application> findForAdmin(Pageable pageable);
}
