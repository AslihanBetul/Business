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

    public void createNotification(String userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false); // Varsayılan olarak okunmamış
        notification.setDeleted(false); // Varsayılan olarak silinmemiş
        notificationRepository.save(notification);

        // WebSocket ile kullanıcıya bildirimi gönder
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
    }

    public List<Notification> getNotifications(String userId) {
        return notificationRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setDeleted(true);
        notificationRepository.save(notification);
    }
}
