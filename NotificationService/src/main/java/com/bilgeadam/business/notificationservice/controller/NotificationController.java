package com.bilgeadam.business.notificationservice.controller;

import com.bilgeadam.business.notificationservice.dto.request.NotificationRequestDto;
import com.bilgeadam.business.notificationservice.entity.Notification;
import com.bilgeadam.business.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationRequestDto dto) {
        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setMessage(dto.getMessage());
        notificationService.createNotification(dto.getUserId(), dto.getMessage());
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{notificationId}/delete")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
