package com.internly.dto;

import com.internly.entity.Internship; import jakarta.validation.constraints.*; import java.time.LocalDate; import java.util.List;

public final class InternshipDtos {
    private InternshipDtos() {}
    public record CompanyRequest(@NotBlank @Size(max=160) String name, @Size(max=2000) String description, @Size(max=255) String website, @Size(max=120) String location, @Size(max=120) String industry) {}
    public record CompanyResponse(Long id, String name, String description, String website, String location, String industry, boolean active) {}
    public record InternshipRequest(@NotBlank @Size(max=180) String title, @NotBlank @Size(max=2000) String description, @NotNull Long companyId, @NotBlank @Size(max=120) String domain, @Size(max=120) String qualification, @Size(max=500) String eligibleBranches, @Size(max=120) String location, @Size(max=60) String workMode, @Min(1) @Max(104) Integer durationWeeks, @PositiveOrZero Integer stipend, @Future LocalDate applicationDeadline, @NotEmpty List<@NotBlank @Size(max=80) String> requiredSkills, Internship.Status status) {}
    public record InternshipResponse(Long id, String title, String description, CompanyResponse company, String domain, String qualification, String eligibleBranches, String location, String workMode, Integer durationWeeks, Integer stipend, LocalDate applicationDeadline, Internship.Status status, List<String> requiredSkills) {}
}
