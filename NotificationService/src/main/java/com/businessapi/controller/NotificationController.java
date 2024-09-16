package com.businessapi.controller;
import static com.businessapi.constants.EndPoints.*;

import com.businessapi.dto.request.NotificationRequestDto;
import com.businessapi.entity.Notification;
import com.businessapi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(NOTIFICATIONS)
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationRequestDto dto) {
        notificationService.createNotification(dto.getUserId(), dto.getTitle(),  dto.getMessage());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable @RequestParam String userId) {
        List<Notification> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping(GET_ALL_NOTIFICATIONS)
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping(GET_ALL_UNREAD_NOTIFICATIONS)
    public ResponseEntity<List<Notification>> getAllUnReadNotifications() {
        List<Notification> notifications = notificationService.getAllUnReadNotifications();
        return ResponseEntity.ok(notifications);}



        @PatchMapping(READ)
    public ResponseEntity<Void> markAsRead(@PathVariable @RequestParam  Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Void> deleteNotifications(@RequestBody List<Long> notificationIds) {
        notificationService.deleteNotifications(notificationIds);
        return ResponseEntity.noContent().build();
    }


}

