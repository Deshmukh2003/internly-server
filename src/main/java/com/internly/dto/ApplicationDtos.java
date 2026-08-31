package com.internly.dto;

import com.internly.dto.InternshipDtos.InternshipResponse; import com.internly.entity.Application; import jakarta.validation.constraints.*; import java.time.Instant;

public final class ApplicationDtos { private ApplicationDtos() {} public record CreateApplicationRequest(@NotNull Long internshipId, @Size(max=2000) String coverNote) {} public record StatusRequest(@NotNull Application.Status status) {} public record ApplicationResponse(Long id, Long studentId, String studentEmail, InternshipResponse internship, String coverNote, Application.Status status, Instant appliedAt) {} }
