package com.internly.controller;

import com.internly.dto.RecommendationDtos.RecommendationResponse; import com.internly.service.RecommendationService; import org.springframework.data.domain.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/recommendations") public class RecommendationController {
    private final RecommendationService recommendations; public RecommendationController(RecommendationService recommendations) { this.recommendations=recommendations; }
    @GetMapping public Page<RecommendationResponse> list(Authentication auth,@PageableDefault(size=12) Pageable pageable) { return recommendations.refreshAndList(auth.getName(),pageable); }
}
