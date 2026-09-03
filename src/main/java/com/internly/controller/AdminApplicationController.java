package com.internly.controller;

import com.internly.dto.ApplicationDtos.*; import com.internly.service.ApplicationService; import jakarta.validation.Valid; import org.springframework.data.domain.*; import org.springframework.data.web.PageableDefault; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/admin/applications") public class AdminApplicationController { private final ApplicationService applications; public AdminApplicationController(ApplicationService applications) { this.applications=applications; } @GetMapping public Page<ApplicationResponse> list(@PageableDefault(size=20,sort="appliedAt",direction=Sort.Direction.DESC) Pageable pageable) { return applications.adminApplications(pageable); } @PatchMapping("/{id}/status") public ApplicationResponse status(@PathVariable Long id,@Valid @RequestBody StatusRequest request) { return applications.updateStatus(id,request); } }
