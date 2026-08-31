package com.internly.controller;

import com.internly.dto.StudentDtos.*; import com.internly.service.StudentProfileService; import jakarta.validation.Valid; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/student")
public class StudentProfileController {
    private final StudentProfileService profiles; public StudentProfileController(StudentProfileService profiles) { this.profiles=profiles; }
    @GetMapping("/profile") public ProfileResponse getProfile(Authentication auth) { return profiles.get(auth.getName()); }
    @PutMapping("/profile") public ProfileResponse saveProfile(Authentication auth, @Valid @RequestBody ProfileRequest request) { return profiles.save(auth.getName(), request); }
    @GetMapping("/skills") public ProfileResponse getSkills(Authentication auth) { return profiles.get(auth.getName()); }
    @PostMapping("/skills") public SkillResponse addSkill(Authentication auth, @Valid @RequestBody SkillRequest request) { return profiles.addSkill(auth.getName(), request); }
    @DeleteMapping("/skills/{skillId}") public void removeSkill(Authentication auth, @PathVariable Long skillId) { profiles.removeSkill(auth.getName(), skillId); }
    @PostMapping(value="/resume", consumes="multipart/form-data") public ProfileResponse uploadResume(Authentication auth, @RequestPart("file") org.springframework.web.multipart.MultipartFile file) { return profiles.uploadResume(auth.getName(), file); }
    @DeleteMapping("/resume") public void deleteResume(Authentication auth) { profiles.deleteResume(auth.getName()); }
}
