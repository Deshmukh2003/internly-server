package com.internly.repository;

import com.internly.entity.*;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface RecommendationRepository
  extends JpaRepository<Recommendation, Long>
{
  Optional<Recommendation> findByStudentIdAndInternshipId(
    Long studentId,
    Long internshipId
  );

  @Query(
    "select r from Recommendation r join fetch r.internship i join fetch i.company where r.student.id = :studentId order by r.matchScore desc, i.applicationDeadline asc"
  )
  Page<Recommendation> findForStudent(
    @Param("studentId") Long studentId,
    Pageable pageable
  );
}
