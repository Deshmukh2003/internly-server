package com.internly.service;

import com.internly.dto.DummyDataDtos.DummyDataStatus;
import com.internly.entity.*;
import com.internly.repository.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DummyDataService {
  private static final String DATA_SET_KEY = "internly-e2e-fixture-v1";
  private static final List<CompanySeed> COMPANIES = List.of(
    new CompanySeed("Asterbyte Labs", "Technology", "Pune", "Builds practical cloud tools for growing businesses."),
    new CompanySeed("QuarryWorks Systems", "Industrial Software", "Nashik", "Connects shop-floor equipment with real-time operational data."),
    new CompanySeed("Civic Horizon Studio", "Urban Planning", "Nagpur", "Designs people-first public spaces and resilient city infrastructure."),
    new CompanySeed("Voltwise Energy", "Clean Energy", "Bengaluru", "Develops intelligent energy monitoring and electrification products."),
    new CompanySeed("Northstar Robotics", "Robotics", "Hyderabad", "Builds dependable automation for warehouses and manufacturing teams."),
    new CompanySeed("Saffron HealthTech", "Healthcare Technology", "Mumbai", "Creates accessible digital workflows for clinics and care teams."),
    new CompanySeed("BlueMosaic Analytics", "Data Analytics", "Chennai", "Turns operational data into decisions for consumer businesses."),
    new CompanySeed("TerraSpan Projects", "Civil Engineering", "Ahmedabad", "Plans sustainable transport, water, and public-works projects."),
    new CompanySeed("Kinetica Mobility", "Automotive", "Pune", "Develops safer, cleaner systems for next-generation mobility."),
    new CompanySeed("Lumina Circuitry", "Electronics", "Noida", "Designs embedded control platforms for connected devices."),
    new CompanySeed("ForgeLane Manufacturing", "Manufacturing", "Coimbatore", "Modernizes production planning and quality systems."),
    new CompanySeed("Nectar Commerce", "E-commerce", "Gurugram", "Builds thoughtful commerce experiences for independent brands."),
    new CompanySeed("HarborGrid Logistics", "Logistics", "Kochi", "Optimizes routes, warehouses, and shipment visibility."),
    new CompanySeed("Veridian Materials", "Chemical Engineering", "Vadodara", "Improves process safety and materials efficiency."),
    new CompanySeed("Skyline Habitat", "Architecture", "Jaipur", "Designs climate-conscious homes, workplaces, and mixed-use spaces."),
    new CompanySeed("OrbitLearn", "Education Technology", "Delhi", "Makes career learning clearer through adaptive digital products."),
    new CompanySeed("Prism Finance Labs", "Financial Technology", "Mumbai", "Creates trustworthy data products for financial operations."),
    new CompanySeed("Meadow AgriScience", "Agritech", "Indore", "Helps farms make better decisions with sensors and field data."),
    new CompanySeed("Tideway Water", "Water Infrastructure", "Surat", "Builds monitoring systems for efficient water networks."),
    new CompanySeed("Atlas AeroWorks", "Aerospace", "Bengaluru", "Develops test and analysis tools for aerospace engineering teams."),
    new CompanySeed("MotiveMint", "Consumer Technology", "Chandigarh", "Creates simple digital products for everyday mobility."),
    new CompanySeed("Cedar Research Collective", "Research", "Kolkata", "Runs applied research programs across science and society."),
    new CompanySeed("SignalNest Networks", "Telecommunications", "Bengaluru", "Improves network reliability for connected communities."),
    new CompanySeed("Ember Food Systems", "Food Technology", "Mysuru", "Builds efficient, low-waste food production systems."),
    new CompanySeed("Canvas & Column", "Design Services", "Goa", "Combines spatial design, visual systems, and digital experiences.")
  );
  private static final List<RoleTemplate> ROLES = List.of(
    new RoleTemplate("Software Engineering Intern", "Computer Science & Engineering", "B.Tech", List.of("Java", "Spring Boot", "React", "SQL"), List.of("Computer Science & Engineering", "Electronics & Communication"), "Build and test a production feature with a cross-functional product team."),
    new RoleTemplate("Data Analytics Intern", "Computer Science & Engineering", "B.Sc.", List.of("Python", "SQL", "Excel", "Power BI"), List.of("Computer Science & Engineering", "Electrical Engineering"), "Transform raw operational data into clear dashboards and decision-ready insights."),
    new RoleTemplate("Mechanical Design Intern", "Mechanical Engineering", "B.E.", List.of("SolidWorks", "AutoCAD", "GD&T", "Manufacturing"), List.of("Mechanical Engineering", "Chemical Engineering"), "Support concept design, CAD refinement, and design-review documentation."),
    new RoleTemplate("Civil Planning Intern", "Civil Engineering", "B.Tech", List.of("AutoCAD", "Quantity Surveying", "MS Project", "Site Planning"), List.of("Civil Engineering", "B.Arch"), "Assist with drawings, project schedules, and practical site-planning work."),
    new RoleTemplate("Electrical Systems Intern", "Electrical Engineering", "B.E.", List.of("MATLAB", "Circuit Design", "PLC", "Electrical Safety"), List.of("Electrical Engineering", "Electronics & Communication"), "Contribute to test plans and system documentation for reliable electrical products."),
    new RoleTemplate("Embedded IoT Intern", "Electronics & Communication", "B.Tech", List.of("C", "Embedded Systems", "Arduino", "PCB Design"), List.of("Electronics & Communication", "Computer Science & Engineering"), "Prototype connected hardware and validate firmware against real device requirements."),
    new RoleTemplate("Process Engineering Intern", "Chemical Engineering", "B.E.", List.of("Process Simulation", "HYSYS", "Safety Analysis", "Data Analysis"), List.of("Chemical Engineering", "Mechanical Engineering"), "Improve a live process through data review, safety checks, and experiment planning."),
    new RoleTemplate("Product Operations Intern", "Computer Science & Engineering", "BBA", List.of("Excel", "Market Research", "Communication", "Notion"), List.of("Computer Science & Engineering", "Mechanical Engineering", "Civil Engineering"), "Coordinate product research, customer feedback, and operational experiments."),
    new RoleTemplate("Architecture Research Intern", "Civil Engineering", "B.Arch", List.of("Revit", "AutoCAD", "3D Modeling", "Sustainable Design"), List.of("Civil Engineering", "Mechanical Engineering"), "Develop climate-aware spatial studies and presentation-ready design concepts."),
    new RoleTemplate("Applied Research Intern", "Computer Science & Engineering", "B.Sc.", List.of("Python", "Research Methods", "Statistics", "Technical Writing"), List.of("Computer Science & Engineering", "Electrical Engineering", "Chemical Engineering"), "Run a structured research sprint and communicate findings to technical stakeholders."),
    new RoleTemplate("Business Intelligence Intern", "Electrical Engineering", "BCA", List.of("SQL", "Tableau", "Excel", "Data Visualization"), List.of("Computer Science & Engineering", "Electrical Engineering", "Electronics & Communication"), "Create useful reporting workflows from business data and stakeholder questions."),
    new RoleTemplate("Digital Marketing Intern", "Computer Science & Engineering", "BBA", List.of("SEO", "Content Writing", "Google Analytics", "Canva"), List.of("Computer Science & Engineering", "Electronics & Communication", "Civil Engineering"), "Plan, measure, and improve an audience-growth experiment for a live product.")
  );

  private final CompanyRepository companies;
  private final InternshipRepository internships;
  private final SkillRepository skills;
  private final ApplicationRepository applications;
  private final RecommendationRepository recommendations;
  private final NotificationRepository notifications;

  public DummyDataService(CompanyRepository companies, InternshipRepository internships, SkillRepository skills, ApplicationRepository applications, RecommendationRepository recommendations, NotificationRepository notifications) {
    this.companies = companies; this.internships = internships; this.skills = skills; this.applications = applications; this.recommendations = recommendations; this.notifications = notifications;
  }

  @Transactional(readOnly = true)
  public DummyDataStatus status() { List<Company> seeded = companies.findAllByDataSetKey(DATA_SET_KEY); return new DummyDataStatus(!seeded.isEmpty(), seeded.size(), internships.findAllByCompanyDataSetKey(DATA_SET_KEY).size()); }

  @Transactional
  public DummyDataStatus add() {
    if (status().seeded()) return status();
    for (int companyIndex = 0; companyIndex < COMPANIES.size(); companyIndex++) {
      CompanySeed seed = COMPANIES.get(companyIndex);
      Company company = companies.save(Company.builder().name(seed.name()).description(seed.description()).industry(seed.industry()).location(seed.location()).website("https://example.com/" + slug(seed.name())).active(true).dataSetKey(DATA_SET_KEY).build());
      for (int roleOffset = 0; roleOffset < 3; roleOffset++) createInternship(company, ROLES.get((companyIndex * 3 + roleOffset) % ROLES.size()), companyIndex, roleOffset, seed);
    }
    return status();
  }

  @Transactional
  public DummyDataStatus remove() {
    List<Internship> seededInternships = internships.findAllByCompanyDataSetKey(DATA_SET_KEY);
    List<Long> ids = seededInternships.stream().map(Internship::getId).toList();
    if (!ids.isEmpty()) { notifications.deleteByInternshipIds(ids); applications.deleteByInternshipIds(ids); recommendations.deleteByInternshipIds(ids); internships.deleteAll(seededInternships); internships.flush(); }
    List<Company> seededCompanies = companies.findAllByDataSetKey(DATA_SET_KEY);
    companies.deleteAll(seededCompanies);
    return new DummyDataStatus(false, 0, 0);
  }

  private void createInternship(Company company, RoleTemplate role, int companyIndex, int roleOffset, CompanySeed seed) {
    Set<Skill> requiredSkills = new HashSet<>();
    for (String name : role.skills()) { String normalized = normalize(name); requiredSkills.add(skills.findByNormalizedName(normalized).orElseGet(() -> skills.save(Skill.builder().name(name).normalizedName(normalized).build()))); }
    internships.save(Internship.builder().company(company).title(role.title() + " — " + seed.name()).description(seed.name() + " is looking for a curious " + role.title().toLowerCase() + ". " + role.description() + " You will work with mentors, share progress each week, and leave with a portfolio-ready outcome.").domain(role.domain()).qualification(role.qualification()).eligibleBranches(new HashSet<>(role.branches())).location(seed.location()).workMode(List.of("On-site", "Remote", "Hybrid").get((companyIndex + roleOffset) % 3)).durationWeeks(List.of(8, 10, 12, 16).get((companyIndex + roleOffset) % 4)).stipend(9000 + ((companyIndex * 3 + roleOffset) % 8) * 1500).applicationDeadline(LocalDate.now().plusDays(35 + companyIndex * 2 + roleOffset * 3)).status(Internship.Status.ACTIVE).requiredSkills(requiredSkills).build());
  }

  private String normalize(String value) { return value.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim(); }
  private String slug(String value) { return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", ""); }
  private record CompanySeed(String name, String industry, String location, String description) {}
  private record RoleTemplate(String title, String domain, String qualification, List<String> skills, List<String> branches, String description) {}
}
