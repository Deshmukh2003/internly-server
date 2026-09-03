package com.internly.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 160)
  private String name;

  @Column(length = 2000)
  private String description;

  private String website;
  private String location;
  private String industry;

  @Column(name = "data_set_key", length = 64)
  private String dataSetKey;

  private boolean active = true;
}
