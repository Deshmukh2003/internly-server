package com.internly.repository;

import com.internly.entity.Company; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;

public interface CompanyRepository extends JpaRepository<Company, Long> { Optional<Company> findByNameIgnoreCase(String name); }
