package com.internly.constants;

import java.util.Set;

public final class StudentProfileOptions {

  public static final Set<String> DOMAINS = Set.of(
    "Computer Science",
    "Mechanical",
    "Civil",
    "Electrical",
    "Electronics & Communication",
    "Chemical"
  );
  public static final Set<String> QUALIFICATIONS = Set.of(
    "B.E.",
    "B.Tech",
    "B.Sc.",
    "BCA"
  );

  private StudentProfileOptions() {}
}
