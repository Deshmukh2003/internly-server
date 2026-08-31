package com.internly.service;

import com.internly.dto.InternshipDtos.*; import com.internly.entity.Company; import com.internly.repository.CompanyRepository; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

@Service public class CompanyService {
    private final CompanyRepository companies; public CompanyService(CompanyRepository companies) { this.companies=companies; }
    @Transactional(readOnly=true) public java.util.List<CompanyResponse> list() { return companies.findAll().stream().map(this::response).toList(); }
    @Transactional public CompanyResponse create(CompanyRequest r) { if (companies.findByNameIgnoreCase(r.name().trim()).isPresent()) throw new IllegalArgumentException("A company with this name already exists"); return response(companies.save(Company.builder().name(r.name().trim()).description(clean(r.description())).website(clean(r.website())).location(clean(r.location())).industry(clean(r.industry())).active(true).build())); }
    @Transactional public CompanyResponse update(Long id, CompanyRequest r) { Company c = companies.findById(id).orElseThrow(() -> new IllegalArgumentException("Company not found")); c.setName(r.name().trim()); c.setDescription(clean(r.description())); c.setWebsite(clean(r.website())); c.setLocation(clean(r.location())); c.setIndustry(clean(r.industry())); return response(companies.save(c)); }
    @Transactional public void deactivate(Long id) { Company c=companies.findById(id).orElseThrow(() -> new IllegalArgumentException("Company not found")); c.setActive(false); companies.save(c); }
    private CompanyResponse response(Company c) { return new CompanyResponse(c.getId(),c.getName(),c.getDescription(),c.getWebsite(),c.getLocation(),c.getIndustry(),c.isActive()); }
    private String clean(String v) { return v == null || v.isBlank() ? null : v.trim(); }
}
