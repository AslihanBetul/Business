package com.businessapi.service;

import com.businessapi.entity.Notification;
import com.businessapi.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void createNotification(Long userId, String title, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setDeleted(false);

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/create-notifications", notification);

        updateUnreadCount();
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/markasread-notifications", notification);

        updateUnreadCount();
    }

    public void deleteNotifications(List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        if (notifications.isEmpty()) {
            throw new RuntimeException("No notifications found with the given IDs");
        }

        notificationRepository.deleteAll(notifications);

        for (Notification notification : notifications) {
            messagingTemplate.convertAndSend("/topic/delete-notifications", notification);
        }

        updateUnreadCount();
    }

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsDeletedFalse(userId);
    }


    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByIsDeletedFalse();
    }


    public List<Notification> getAllUnReadNotifications() {
        return notificationRepository.findByIsReadFalseAndIsDeletedFalse();
    }


    private void updateUnreadCount() {
        long unreadCount = getUnreadNotificationCount();
        messagingTemplate.convertAndSend("/topic/unreadNotifications", unreadCount);
    }


    public long getUnreadNotificationCount() {
        return notificationRepository.countByIsReadFalseAndIsDeletedFalse();
    }


}
