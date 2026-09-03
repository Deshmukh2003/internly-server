package com.internly.repository;

import com.internly.entity.Notification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository
  extends JpaRepository<Notification, Long>
{
  @Modifying
  @Query("delete from Notification n where n.internship.id in :internshipIds")
  void deleteByInternshipIds(@Param("internshipIds") java.util.List<Long> internshipIds);
  @Query(
    "select n from Notification n where n.student.id = :studentId order by n.createdAt desc"
  )
  Page<Notification> findForStudent(
    @Param("studentId") Long studentId,
    Pageable pageable
  );

  long countByStudentIdAndReadAtIsNull(Long studentId);
}
