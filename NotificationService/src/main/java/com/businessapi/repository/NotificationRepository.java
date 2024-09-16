package com.businessapi.repository;

import com.businessapi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndIsDeletedFalse(String userId);
    List<Notification> findByIsReadFalse(); // Okunmayan bildirimleri bulur

    List<Notification> findByIsDeleted(boolean isDeleted); // Silinmemiş bildirimleri getiren yöntem
}
