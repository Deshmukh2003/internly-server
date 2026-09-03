package com.internly.service;

import com.internly.constants.StudentProfileOptions;
import com.internly.dto.InternshipDtos.*;
import com.internly.entity.*;
import com.internly.repository.*;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InternshipService {

  private final InternshipRepository internships;
  private final CompanyRepository companies;
  private final SkillRepository skills;

  public InternshipService(
    InternshipRepository internships,
    CompanyRepository companies,
    SkillRepository skills
  ) {
    this.internships = internships;
    this.companies = companies;
    this.skills = skills;
  }

  @Transactional(readOnly = true)
  public Page<InternshipResponse> browse(
    String search,
    String domain,
    String location,
    Pageable pageable
  ) {
    return internships
      .browse(
        Internship.Status.ACTIVE,
        clean(domain),
        clean(location),
        clean(search),
        pageable
      )
      .map(this::response);
  }

  @Transactional(readOnly = true)
  public List<InternshipResponse> adminList() {
    return internships
      .findAll(Sort.by(Sort.Direction.DESC, "id"))
      .stream()
      .map(this::response)
      .toList();
  }

  @Transactional(readOnly = true)
  public InternshipResponse getActive(Long id) {
    return response(
      internships
        .findByIdAndStatus(id, Internship.Status.ACTIVE)
        .orElseThrow(() -> new IllegalArgumentException("Internship not found"))
    );
  }

  public InternshipResponse toResponseForRecommendation(Internship internship) {
    return response(internship);
  }

  @Transactional
  public InternshipResponse create(InternshipRequest r) {
    return response(save(new Internship(), r));
  }

  @Transactional
  public InternshipResponse update(Long id, InternshipRequest r) {
    return response(
      save(
        internships
          .findById(id)
          .orElseThrow(() ->
            new IllegalArgumentException("Internship not found")
          ),
        r
      )
    );
  }

  @Transactional
  public void deactivate(Long id) {
    Internship i = internships
      .findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Internship not found"));
    i.setStatus(Internship.Status.INACTIVE);
    internships.save(i);
  }

  private Internship save(Internship i, InternshipRequest r) {
    i.setTitle(r.title().trim());
    i.setDescription(r.description().trim());
    i.setCompany(
      companies
        .findById(r.companyId())
        .orElseThrow(() -> new IllegalArgumentException("Company not found"))
    );
    i.setDomain(r.domain().trim());
    String qualification = clean(r.qualification());
    if (
      qualification != null &&
      !StudentProfileOptions.QUALIFICATIONS.contains(qualification)
    ) throw new IllegalArgumentException("Please select a valid qualification");
    Set<String> branches = new HashSet<>(
      r.eligibleBranches() == null ? List.of() : r.eligibleBranches()
    );
    if (
      !StudentProfileOptions.DOMAINS.containsAll(branches)
    ) throw new IllegalArgumentException(
      "Please select valid eligible branches"
    );
    i.setQualification(qualification);
    i.setEligibleBranches(branches);
    String workMode = clean(r.workMode());
    if (
      workMode != null &&
      !Set.of("On-site", "Remote", "Hybrid").contains(workMode)
    ) throw new IllegalArgumentException("Please select a valid work mode");
    i.setLocation(clean(r.location()));
    i.setWorkMode(workMode);
    i.setDurationWeeks(r.durationWeeks());
    i.setStipend(r.stipend());
    i.setApplicationDeadline(r.applicationDeadline());
    i.setStatus(r.status() == null ? Internship.Status.DRAFT : r.status());
    Set<Skill> required = new HashSet<>();
    for (String name : r.requiredSkills()) {
      String normalized = normalize(name);
      required.add(
        skills
          .findByNormalizedName(normalized)
          .orElseGet(() ->
            skills.save(
              Skill.builder()
                .name(name.trim())
                .normalizedName(normalized)
                .build()
            )
          )
      );
    }
    i.setRequiredSkills(required);
    return internships.save(i);
  }

  private InternshipResponse response(Internship i) {
    return new InternshipResponse(
      i.getId(),
      i.getTitle(),
      i.getDescription(),
      new CompanyResponse(
        i.getCompany().getId(),
        i.getCompany().getName(),
        i.getCompany().getDescription(),
        i.getCompany().getWebsite(),
        i.getCompany().getLocation(),
        i.getCompany().getIndustry(),
        i.getCompany().isActive()
      ),
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
  }

  private String normalize(String v) {
    return v.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
  }

  private String clean(String v) {
    return v == null || v.isBlank() ? null : v.trim();
  }
}
