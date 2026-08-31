package com.internly.service;

import com.internly.dto.StudentDtos.*; import com.internly.entity.*; import com.internly.integration.storage.FileStorageService; import com.internly.repository.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.multipart.MultipartFile; import java.io.IOException; import java.time.Instant;
import java.util.*;

@Service
public class StudentProfileService {
    private final UserRepository users; private final StudentProfileRepository profiles; private final SkillRepository skills; private final FileStorageService storage;
    public StudentProfileService(UserRepository users, StudentProfileRepository profiles, SkillRepository skills, FileStorageService storage) { this.users=users; this.profiles=profiles; this.skills=skills; this.storage=storage; }
    @Transactional(readOnly=true) public ProfileResponse get(String email) { return toResponse(profileFor(email)); }
    @Transactional public ProfileResponse save(String email, ProfileRequest request) {
        StudentProfile profile = profileFor(email); profile.setFullName(request.fullName().trim()); profile.setMobile(clean(request.mobile())); profile.setDomain(clean(request.domain())); profile.setQualification(clean(request.qualification())); profile.setCollege(clean(request.college())); profile.setGraduationYear(request.graduationYear()); profile.setInterests(clean(request.interests())); return toResponse(profiles.save(profile));
    }
    @Transactional public SkillResponse addSkill(String email, SkillRequest request) {
        StudentProfile profile = profileFor(email); String normalized = normalize(request.name()); Skill skill = skills.findByNormalizedName(normalized).orElseGet(() -> skills.save(Skill.builder().name(request.name().trim()).normalizedName(normalized).build())); profile.getSkills().add(skill); profiles.save(profile); return new SkillResponse(skill.getId(), skill.getName());
    }
    @Transactional public void removeSkill(String email, Long skillId) { StudentProfile profile = profileFor(email); profile.getSkills().removeIf(skill -> Objects.equals(skill.getId(), skillId)); profiles.save(profile); }
    @Transactional public ProfileResponse uploadResume(String email, MultipartFile file) { validateResume(file); StudentProfile profile=profileFor(email); try { if(profile.getResumeStorageKey()!=null) storage.delete(profile.getResumeStorageKey()); profile.setResumeStorageKey(storage.store(file)); profile.setResumeFileName(file.getOriginalFilename()); profile.setResumeUploadedAt(Instant.now()); return toResponse(profiles.save(profile)); } catch(IOException ex) { throw new IllegalArgumentException("Unable to store resume"); } }
    @Transactional public void deleteResume(String email) { StudentProfile profile=profileFor(email); try { storage.delete(profile.getResumeStorageKey()); } catch(IOException ex) { throw new IllegalArgumentException("Unable to delete resume"); } profile.setResumeStorageKey(null); profile.setResumeFileName(null); profile.setResumeUploadedAt(null); profiles.save(profile); }
    private StudentProfile profileFor(String email) { User user = users.findByEmailIgnoreCase(email).orElseThrow(); return profiles.findByUserId(user.getId()).orElseGet(() -> profiles.save(StudentProfile.builder().userId(user.getId()).user(user).skills(new HashSet<>()).build())); }
    private ProfileResponse toResponse(StudentProfile p) { List<SkillResponse> skillResponses = p.getSkills().stream().sorted(Comparator.comparing(Skill::getName, String.CASE_INSENSITIVE_ORDER)).map(s -> new SkillResponse(s.getId(), s.getName())).toList(); return new ProfileResponse(p.getUserId(), p.getUser().getEmail(), p.getFullName(), p.getMobile(), p.getDomain(), p.getQualification(), p.getCollege(), p.getGraduationYear(), p.getInterests(), p.getResumeFileName(), p.getResumeUploadedAt(), skillResponses); }
    private void validateResume(MultipartFile file) { if(file==null||file.isEmpty()) throw new IllegalArgumentException("Please select a resume"); if(file.getSize()>5*1024*1024) throw new IllegalArgumentException("Resume must be 5 MB or smaller"); String name=file.getOriginalFilename()==null?"":file.getOriginalFilename().toLowerCase(Locale.ROOT); if(!(name.endsWith(".pdf")||name.endsWith(".doc")||name.endsWith(".docx"))) throw new IllegalArgumentException("Resume must be PDF, DOC, or DOCX"); }
    private String normalize(String value) { return value.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT); }
    private String clean(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
