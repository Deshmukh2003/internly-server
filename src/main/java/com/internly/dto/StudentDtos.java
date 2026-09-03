package com.internly.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public final class StudentDtos {

  private StudentDtos() {}

  public record ProfileRequest(
    @NotBlank @Size(max = 120) String fullName,
    @Pattern(regexp = "^[0-9+() -]{7,20}$") String mobile,
    @Pattern(
      regexp = "^(|Computer Science & Engineering|Mechanical Engineering|Civil Engineering|Electrical Engineering|Electronics & Communication|Chemical Engineering)$"
    ) String domain,
    @Pattern(
      regexp = "^(|B\\.E\\.|B\\.Tech|B\\.Sc\\.|BCA|BBA|B\\.Arch)$"
    ) String qualification,
    @Size(max = 160) String college,
    @Min(1950) @Max(2200) Integer graduationYear,
    @Size(max = 1000) String interests
  ) {}

  public record SkillRequest(@NotBlank @Size(max = 80) String name) {}

  public record SkillResponse(Long id, String name) {}

  public record ProfileResponse(
    Long userId,
    String email,
    String fullName,
    String mobile,
    String domain,
    String qualification,
    String college,
    Integer graduationYear,
    String interests,
    String resumeFileName,
    java.time.Instant resumeUploadedAt,
    List<SkillResponse> skills
  ) {}
}
