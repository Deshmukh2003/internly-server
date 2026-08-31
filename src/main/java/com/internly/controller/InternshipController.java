package com.internly.controller;

import com.internly.dto.InternshipDtos.InternshipResponse; import com.internly.service.InternshipService; import org.springframework.data.domain.*; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/internships") public class InternshipController {
    private final InternshipService internships; public InternshipController(InternshipService internships) { this.internships=internships; }
    @GetMapping public Page<InternshipResponse> browse(@RequestParam(required=false) String search,@RequestParam(required=false) String domain,@RequestParam(required=false) String location,@PageableDefault(size=12,sort="applicationDeadline",direction=Sort.Direction.ASC) Pageable pageable) { return internships.browse(search,domain,location,pageable); }
    @GetMapping("/{id}") public InternshipResponse detail(@PathVariable Long id) { return internships.getActive(id); }
}
