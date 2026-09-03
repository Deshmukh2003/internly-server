package com.internly.repository;

import com.internly.entity.Company;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
  Optional<Company> findByNameIgnoreCase(String name);
  List<Company> findAllByDataSetKey(String dataSetKey);
}
