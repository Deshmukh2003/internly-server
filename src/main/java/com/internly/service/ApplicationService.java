package com.internly.service;

import com.internly.dto.ApplicationDtos.*;
import com.internly.dto.InternshipDtos.CompanyResponse;
import com.internly.dto.InternshipDtos.InternshipResponse;
import com.internly.entity.*;
import com.internly.repository.*;
import java.time.LocalDate;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {

  private final UserRepository users;
  private final StudentProfileRepository profiles;
  private final InternshipRepository internships;
  private final ApplicationRepository applications;
  private final InternshipService internshipService;
  private final NotificationService notifications;

  public ApplicationService(
    UserRepository users,
    StudentProfileRepository profiles,
    InternshipRepository internships,
    ApplicationRepository applications,
    InternshipService internshipService,
    NotificationService notifications
  ) {
    this.users = users;
    this.profiles = profiles;
    this.internships = internships;
    this.applications = applications;
    this.internshipService = internshipService;
    this.notifications = notifications;
  }

  @Transactional
  public ApplicationResponse apply(
    String email,
    CreateApplicationRequest request
  ) {
    User student = users.findByEmailIgnoreCase(email).orElseThrow();
    StudentProfile profile = profiles
      .findByUserId(student.getId())
      .orElse(null);
    if (!isProfileComplete(profile)) throw new IllegalArgumentException(
      "Complete your profile, add at least one skill, and upload your resume before applying"
    );
    Internship internship = internships
      .findById(request.internshipId())
      .orElseThrow(() -> new IllegalArgumentException("Internship not found"));
    if (
      internship.getStatus() != Internship.Status.ACTIVE ||
      internship.getApplicationDeadline() == null ||
      internship.getApplicationDeadline().isBefore(LocalDate.now())
    ) throw new IllegalArgumentException(
      "This internship is no longer accepting applications"
    );
    if (
      applications.existsByStudentIdAndInternshipId(
        student.getId(),
        internship.getId()
      )
    ) throw new IllegalArgumentException(
      "You have already applied to this internship"
    );
    Application saved = applications.save(
      Application.builder()
        .student(student)
        .internship(internship)
        .coverNote(clean(request.coverNote()))
        .status(Application.Status.SUBMITTED)
        .build()
    );
    notifications.create(
      student,
      "Application submitted",
      "Your application for " +
        internship.getTitle() +
        " was submitted successfully."
    );
    return response(saved);
  }

  @Transactional(readOnly = true)
  public Page<ApplicationResponse> studentApplications(
    String email,
    Pageable pageable
  ) {
    User student = users.findByEmailIgnoreCase(email).orElseThrow();
    return applications
      .findForStudent(student.getId(), pageable)
      .map(this::response);
  }

  @Transactional(readOnly = true)
  public Page<ApplicationResponse> adminApplications(Pageable pageable) {
    return applications.findForAdmin(pageable).map(this::response);
  }

  @Transactional
  public ApplicationResponse updateStatus(Long id, StatusRequest request) {
    Application application = applications
      .findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    application.setStatus(request.status());
    ApplicationResponse result = response(applications.save(application));
    notifications.create(
      application.getStudent(),
      "Application status updated",
      "Your application for " +
        application.getInternship().getTitle() +
        " is now " +
        request.status().name().toLowerCase().replace('_', ' ') +
        "."
    );
    return result;
  }

  private ApplicationResponse response(Application a) {
    Internship i = a.getInternship();
    CompanyResponse company = new CompanyResponse(
      i.getCompany().getId(),
      i.getCompany().getName(),
      i.getCompany().getDescription(),
      i.getCompany().getWebsite(),
      i.getCompany().getLocation(),
      i.getCompany().getIndustry(),
      i.getCompany().isActive()
    );
    InternshipResponse internship = new InternshipResponse(
      i.getId(),
      i.getTitle(),
      i.getDescription(),
      company,
      i.getDomain(),
      i.getQualification(),
      i
        .getEligibleBranches()
        .stream()
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList(),
      i.getLocation(),
      i.getWorkMode(),
      i.getDurationWeeks(),
      i.getStipend(),
      i.getApplicationDeadline(),
      i.getStatus(),
      i
        .getRequiredSkills()
        .stream()
        .map(Skill::getName)
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList()
    );
    return new ApplicationResponse(
      a.getId(),
      a.getStudent().getId(),
      a.getStudent().getEmail(),
      internship,
      a.getCoverNote(),
      a.getStatus(),
      a.getAppliedAt()
    );
  }

  private String clean(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }

  private boolean isProfileComplete(StudentProfile profile) {
    return (
      profile != null &&
      !blank(profile.getFullName()) &&
      !blank(profile.getDomain()) &&
      !blank(profile.getQualification()) &&
      !blank(profile.getCollege()) &&
      profile.getGraduationYear() != null &&
      !blank(profile.getResumeFileName()) &&
      !profile.getSkills().isEmpty()
    );
  }

  private boolean blank(String value) {
    return value == null || value.isBlank();
  }
}
