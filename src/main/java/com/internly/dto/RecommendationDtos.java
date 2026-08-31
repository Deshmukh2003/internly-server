package com.internly.dto;

import com.internly.dto.InternshipDtos.InternshipResponse; import java.time.Instant;

public final class RecommendationDtos { private RecommendationDtos() {} public record RecommendationResponse(Long id, InternshipResponse internship, int matchScore, long matchedSkills, int requiredSkills, boolean domainMatched, boolean qualificationMatched, String explanation, Instant generatedAt) {} }
