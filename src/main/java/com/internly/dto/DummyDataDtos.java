package com.internly.dto;

public final class DummyDataDtos {
  private DummyDataDtos() {}
  public record DummyDataStatus(boolean seeded, int companyCount, int internshipCount) {}
}
