package com.internly.service;

import com.internly.dto.RecommendationDtos.RecommendationResponse;
import com.internly.entity.*;
import com.internly.repository.*;
import java.time.Instant;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendationService {

  private final UserRepository users;
  private final StudentProfileRepository profiles;
  private final InternshipRepository internships;
  private final RecommendationRepository recommendations;
  private final MatchingEngine engine;
  private final InternshipService internshipService;

  public RecommendationService(
    UserRepository users,
    StudentProfileRepository profiles,
    InternshipRepository internships,
    RecommendationRepository recommendations,
    MatchingEngine engine,
    InternshipService internshipService
  ) {
    this.users = users;
    this.profiles = profiles;
    this.internships = internships;
    this.recommendations = recommendations;
    this.engine = engine;
    this.internshipService = internshipService;
  }

  @Transactional
  public Page<RecommendationResponse> refreshAndList(
    String email,
    Pageable pageable
  ) {
    User student = users.findByEmailIgnoreCase(email).orElseThrow();
    StudentProfile profile = profiles
      .findByUserId(student.getId())
      .orElseGet(() ->
        profiles.save(StudentProfile.builder().user(student).build())
      );
    for (Internship internship : internships
      .findByStatus(Internship.Status.ACTIVE, PageRequest.of(0, 1000))
      .getContent()) {
      MatchingEngine.MatchResult score = engine.score(profile, internship);
      Recommendation r = recommendations
        .findByStudentIdAndInternshipId(student.getId(), internship.getId())
        .orElseGet(() ->
          Recommendation.builder()
            .student(student)
            .internship(internship)
            .build()
        );
      r.setMatchScore(score.score());
      r.setMatchedSkills(score.matchedSkills());
      r.setRequiredSkills(score.requiredSkills());
      r.setDomainMatched(score.domainMatched());
      r.setQualificationMatched(score.qualificationMatched());
      r.setExplanation(score.explanation());
      r.setGeneratedAt(Instant.now());
      recommendations.save(r);
    }
    return recommendations
      .findForStudent(student.getId(), pageable)
      .map(this::response);
  }

  private RecommendationResponse response(Recommendation r) {
    return new RecommendationResponse(
      r.getId(),
      internshipService.toResponseForRecommendation(r.getInternship()),
      r.getMatchScore(),
      r.getMatchedSkills(),
      r.getRequiredSkills(),
      r.isDomainMatched(),
      r.isQualificationMatched(),
      r.getExplanation(),
      r.getGeneratedAt()
    );
  }
}
