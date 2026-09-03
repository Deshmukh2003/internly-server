package com.internly.dto;

import java.time.Instant;

public final class NotificationDtos {

  private NotificationDtos() {}

  public record NotificationResponse(
    Long id,
    String title,
    String message,
    Instant createdAt,
    Instant readAt
  ) {}
}
