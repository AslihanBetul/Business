package com.businessapi.controller;

import static com.businessapi.constants.EndPoints.*;

import com.businessapi.dto.request.NotificationRequestDto;
import com.businessapi.entity.Notification;
import com.businessapi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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


    @PostMapping(CREATE_NOTIFICATION)
    @MessageMapping("/notifications/create")
    @SendTo("/topic/create-notifications")
    public ResponseEntity<Void> createNotification(@RequestBody NotificationRequestDto dto) {
        notificationService.createNotification(dto.getUserId(), dto.getTitle(), dto.getMessage());
        return ResponseEntity.ok().build();
    }


    @GetMapping(GET_NOTIFICATION_FOR_USERID)
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long userId) {
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
        return ResponseEntity.ok(notifications);
    }


    @PatchMapping(READ)
    @MessageMapping("/notifications/markasread")
    @SendTo("/topic/markasread-notifications")
    public ResponseEntity<Void> markAsRead(@RequestParam Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(DELETE)
    @MessageMapping("/notifications/delete")
    @SendTo("/topic/delete-notifications")
    public ResponseEntity<Void> deleteNotifications(@RequestBody List<Long> notificationIds) {
        notificationService.deleteNotifications(notificationIds);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(GET_UNREAD_COUNT)
    public ResponseEntity<Long> getUnreadNotificationCount() {
        long unreadCount = notificationService.getUnreadNotificationCount();
        return ResponseEntity.ok(unreadCount);
    }


}
