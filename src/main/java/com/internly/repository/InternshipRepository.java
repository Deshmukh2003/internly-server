package com.internly.repository;

import com.internly.entity.Internship; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    Page<Internship> findByStatus(Internship.Status status, Pageable pageable);
    java.util.Optional<Internship> findByIdAndStatus(Long id, Internship.Status status);
    @Query("select i from Internship i where i.status = :status and (:domain is null or lower(i.domain) = lower(:domain)) and (:location is null or lower(i.location) like lower(concat('%', :location, '%'))) and (:search is null or lower(i.title) like lower(concat('%', :search, '%')) or lower(i.description) like lower(concat('%', :search, '%')))" )
    Page<Internship> browse(@Param("status") Internship.Status status, @Param("domain") String domain, @Param("location") String location, @Param("search") String search, Pageable pageable);
}
