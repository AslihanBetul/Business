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
        notification.setRead(false); // Varsayılan olarak okunmamış
        notification.setDeleted(false); // Varsayılan olarak silinmemiş
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/create-notifications", notification);

    }

   //belirli bir kullanıcıya ait olan ve silinmemiş bildirimleri döndürür
    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsDeletedFalse(userId);
    }
    // Tüm silinmemiş bildirimleri getir
    public List<Notification> getAllNotifications() {

        return notificationRepository.findByIsDeleted(false);
    }
    public List<Notification> getAllUnReadNotifications() {
        return notificationRepository.findByIsReadFalseAndIsDeletedFalse(); // Okunmayan bildirimleri döndürür
    }
    // Okundu olarak işaretle
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/markasread-notifications", notification);
    }

    public void deleteNotifications(List<Long> notificationIds) {
        // Veritabanından bildirimleri bul
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        // Eğer hiç bildirim bulunamazsa hata fırlat
        if (notifications.isEmpty()) {
            throw new RuntimeException("No notifications found with the given IDs");
        }

        // Bildirimleri sil
        notificationRepository.deleteAll(notifications); // Burada tam silme işlemi gerçekleştiriliyor

        // İsteğe bağlı: Silinen bildirimleri istemcilere ilet
        for (Notification notification : notifications) {
            messagingTemplate.convertAndSend("/topic/delete-notifications", notification); // Silindiğini bildirin
        }
    }


    // Okunmamış bildirimlerin sayısını döndüren metot
    public long getUnreadNotificationCount() {
        return notificationRepository.countByIsReadFalseAndIsDeletedFalse(); // "read" alanı false olan bildirimlerin sayısını döndürür
    }
}
