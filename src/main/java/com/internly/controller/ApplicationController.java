package com.internly.controller;

import com.internly.dto.ApplicationDtos.*; import com.internly.service.ApplicationService; import jakarta.validation.Valid; import org.springframework.data.domain.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/applications") public class ApplicationController { private final ApplicationService applications; public ApplicationController(ApplicationService applications) { this.applications=applications; } @PostMapping public ApplicationResponse apply(Authentication auth,@Valid @RequestBody CreateApplicationRequest request) { return applications.apply(auth.getName(),request); } @GetMapping public Page<ApplicationResponse> list(Authentication auth,@PageableDefault(size=12,sort="appliedAt",direction=Sort.Direction.DESC) Pageable pageable) { return applications.studentApplications(auth.getName(),pageable); } }
