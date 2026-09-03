package com.internly.controller;

import com.internly.dto.InternshipDtos.*;
import com.internly.dto.DummyDataDtos.DummyDataStatus;
import com.internly.service.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminManagementController {

  private final CompanyService companies;
  private final InternshipService internships;
  private final DummyDataService dummyData;

  public AdminManagementController(
    CompanyService companies,
    InternshipService internships,
    DummyDataService dummyData
  ) {
    this.companies = companies;
    this.internships = internships;
    this.dummyData = dummyData;
  }

  @GetMapping("/companies")
  public List<CompanyResponse> companies() {
    return companies.list();
  }

  @PostMapping("/companies")
  public CompanyResponse createCompany(@Valid @RequestBody CompanyRequest r) {
    return companies.create(r);
  }

  @PutMapping("/companies/{id}")
  public CompanyResponse updateCompany(
    @PathVariable Long id,
    @Valid @RequestBody CompanyRequest r
  ) {
    return companies.update(id, r);
  }

  @DeleteMapping("/companies/{id}")
  public void deactivateCompany(@PathVariable Long id) {
    companies.deactivate(id);
  }

  @GetMapping("/internships")
  public List<InternshipResponse> internships() {
    return internships.adminList();
  }

  @PostMapping("/internships")
  public InternshipResponse createInternship(
    @Valid @RequestBody InternshipRequest r
  ) {
    return internships.create(r);
  }

  @PutMapping("/internships/{id}")
  public InternshipResponse updateInternship(
    @PathVariable Long id,
    @Valid @RequestBody InternshipRequest r
  ) {
    return internships.update(id, r);
  }

  @DeleteMapping("/internships/{id}")
  public void deactivateInternship(@PathVariable Long id) {
    internships.deactivate(id);
  }

  @GetMapping("/dummy-data")
  public DummyDataStatus dummyDataStatus() { return dummyData.status(); }

  @PostMapping("/dummy-data")
  public DummyDataStatus addDummyData() { return dummyData.add(); }

  @DeleteMapping("/dummy-data")
  public DummyDataStatus removeDummyData() { return dummyData.remove(); }
}
