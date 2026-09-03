package com.internly.service;

import com.internly.dto.NotificationDtos.NotificationResponse;
import com.internly.entity.*;
import com.internly.repository.*;
import java.time.Instant;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

  private final UserRepository users;
  private final NotificationRepository notifications;

  public NotificationService(
    UserRepository users,
    NotificationRepository notifications
  ) {
    this.users = users;
    this.notifications = notifications;
  }

  @Transactional
  public void create(User student, String title, String message) {
    notifications.save(
      Notification.builder()
        .student(student)
        .title(title)
        .message(message)
        .build()
    );
  }

  @Transactional(readOnly = true)
  public Page<NotificationResponse> list(String email, Pageable pageable) {
    User student = users.findByEmailIgnoreCase(email).orElseThrow();
    return notifications
      .findForStudent(student.getId(), pageable)
      .map(this::response);
  }

  @Transactional
  public NotificationResponse markRead(String email, Long id) {
    User student = users.findByEmailIgnoreCase(email).orElseThrow();
    Notification n = notifications
      .findById(id)
      .filter(item -> item.getStudent().getId().equals(student.getId()))
      .orElseThrow(() ->
        new IllegalArgumentException("Notification not found")
      );
    if (n.getReadAt() == null) n.setReadAt(Instant.now());
    return response(notifications.save(n));
  }

  @Transactional(readOnly = true)
  public long unreadCount(String email) {
    return users
      .findByEmailIgnoreCase(email)
      .map(user -> notifications.countByStudentIdAndReadAtIsNull(user.getId()))
      .orElse(0L);
  }

  private NotificationResponse response(Notification n) {
    return new NotificationResponse(
      n.getId(),
      n.getTitle(),
      n.getMessage(),
      n.getCreatedAt(),
      n.getReadAt()
    );
  }
}
