package com.businessapi.service;

import com.businessapi.dto.request.EventDeleteRequestDTO;
import com.businessapi.dto.request.EventSaveRequestDTO;
import com.businessapi.dto.request.EventUpdateRequestDTO;
import com.businessapi.entity.Event;
import com.businessapi.entity.InvitedUser;
import com.businessapi.exception.CalendarAndPlannigServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.RabbitMQ.Model.RabbitMQNotification;
import com.businessapi.repository.EventRepository;
import com.businessapi.repository.InvitedUserRepository;
import com.businessapi.util.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final InvitedUserRepository invitedUserRepository;
    private final JwtTokenManager jwtTokenManager;
    private final RabbitTemplate rabbitTemplate;


    @Transactional
    public Boolean save(EventSaveRequestDTO eventSaveRequestDTO) {
        Long authId = extractAuthIdFromToken(eventSaveRequestDTO.token());

        // Event nesnesini oluştur
        Event event = Event.builder()
                .authId(authId)  // Kullanıcı ID'si
                .title(eventSaveRequestDTO.title())
                .description(eventSaveRequestDTO.description())
                .location(eventSaveRequestDTO.location())
                .startDateTime(eventSaveRequestDTO.startDateTime())
                .endDateTime(eventSaveRequestDTO.endDateTime())
                .build();

        Set<InvitedUser> invitedUsers = new HashSet<>();

        // Kullanıcının kendisini davet et
        InvitedUser selfInvitedUser = new InvitedUser();
        selfInvitedUser.setEvent(event); // Kendisi için etkinlik ayarla
        selfInvitedUser.setUserId(authId); // Kendisi
        selfInvitedUser.setStatus("Active"); // Kendisi her zaman aktif
        invitedUsers.add(selfInvitedUser);
        invitedUserRepository.save(selfInvitedUser);

        // Eğer davet edilen kullanıcı ID'leri varsa, onları ekle
        if (eventSaveRequestDTO.invitedUserIds() != null) {
            for (Long userId : eventSaveRequestDTO.invitedUserIds()) {
                InvitedUser invitedUser = new InvitedUser();
                invitedUser.setEvent(event); // Davetli kullanıcı için etkinlik ayarla
                invitedUser.setUserId(userId);
                invitedUser.setStatus("Pending"); // Davet edilen kullanıcılar için varsayılan durum
                invitedUsers.add(invitedUser);
                invitedUserRepository.save(invitedUser);


                // Sadece "Pending" durumundaki kullanıcılar için bildirim gönder
                if ("Pending".equals(invitedUser.getStatus())) {
                    // Bildirim gönderimi
                    rabbitTemplate.convertAndSend("notificationExchange", "notificationKey", RabbitMQNotification.builder().title("Etkinlik Daveti").authIds(List.of(userId)).message("Sizi " + event.getTitle() + " etkinliğine davet ettik.").build());
                }
            }
        }
        eventRepository.save(event);

        return true;
    }
    public List<Event> findAllByAuthId(String token) {
        Long invitedUserId = extractAuthIdFromToken(token);

        // invitedUserId ile eşleşen davetleri al
        List<InvitedUser> invitedUsers = invitedUserRepository.findAllByUserId(invitedUserId);

        if (invitedUsers.isEmpty()) {
            throw new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND);
        }

        // invitedUserId ile eşleşen Event nesnelerini topla
        List<Event> events = new ArrayList<>();
        for (InvitedUser invitedUser : invitedUsers) {
            if (invitedUser.getUserId().equals(invitedUserId)) {
                events.add(invitedUser.getEvent()); // invitedUser'a ait Event'i ekle
            }
        }

        return events;
    }

    public Boolean deleteByCreator(EventDeleteRequestDTO eventDeleteRequestDTO) {

        Event event = eventRepository.findById(eventDeleteRequestDTO.id())
                .orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));

        // 2. Kullanıcı yetkisini doğrula
        Long authId = extractAuthIdFromToken(eventDeleteRequestDTO.token());
        if (!event.getAuthId().equals(authId)) {
            throw new CalendarAndPlannigServiceException(ErrorType.BAD_REQUEST_ERROR);
        }

        // 3. Davetli kullanıcıları etkinlik ID'sine göre sil
        invitedUserRepository.deleteByEventId(eventDeleteRequestDTO.id()); // Etkinlik ID'sine bağlı davetli kullanıcıları sil

        // 4. Etkinliği sil
        eventRepository.deleteById(eventDeleteRequestDTO.id());

        return true; // Başarılı silme işlemi
    }




    public Boolean deleteByInvitee(EventDeleteRequestDTO dto) {

        Event event = eventRepository.findById(dto.id())
                .orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));

        Long authId = extractAuthIdFromToken(dto.token());

        // 3. Davet edilen kullanıcıyı etkinlik ve kullanıcı ID'sine göre bul
        InvitedUser invitedUser = invitedUserRepository.findByEventAndUserId(event, authId)
                .orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.INVALID_INVITATION_ERROR));

        // 4. Davet edilen kullanıcıyı sil
        invitedUserRepository.delete(invitedUser);

        return true; // Başarılı silme işlemi
    }


    public Boolean update(EventUpdateRequestDTO eventUpdateRequestDTO) {
        Event event = eventRepository.findById(eventUpdateRequestDTO.id())
                .orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));

        Long authId = extractAuthIdFromToken(eventUpdateRequestDTO.token());

        if (!event.getAuthId().equals(authId)) {
            throw new CalendarAndPlannigServiceException(ErrorType.EVENT_UPDATE_PERMISSION_ERROR);
        }

        event.setTitle(eventUpdateRequestDTO.title() != null ? eventUpdateRequestDTO.title() : event.getTitle());
        event.setDescription(eventUpdateRequestDTO.description() != null ? eventUpdateRequestDTO.description() : event.getDescription());
        event.setLocation(eventUpdateRequestDTO.location() != null ? eventUpdateRequestDTO.location() : event.getLocation());
        event.setStartDateTime(eventUpdateRequestDTO.startDateTime() != null ? eventUpdateRequestDTO.startDateTime() : event.getStartDateTime());
        event.setEndDateTime(eventUpdateRequestDTO.endDateTime() != null ? eventUpdateRequestDTO.endDateTime() : event.getEndDateTime());

        // Davet edilen kullanıcıları güncelle
        if (eventUpdateRequestDTO.invitedUserIds() != null) {
            Set<InvitedUser> invitedUsers = new HashSet<>();
            for (Long userId : eventUpdateRequestDTO.invitedUserIds()) {
                InvitedUser invitedUser = new InvitedUser();
                invitedUser.setEvent(event);
                invitedUser.setStatus("Pending"); // Yeni davet edilenler varsayılan olarak "Pending" olur
                invitedUsers.add(invitedUser);
            }
            invitedUserRepository.saveAll(invitedUsers); // Yeni davet edilen kullanıcıları kaydet
        }

        eventRepository.save(event);
        return true;
    }

//    public Boolean approveEvent(String eventId, String token) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));
//
//        Long authId = extractAuthIdFromToken(token);
//
//        // Kullanıcının etkinliği onaylama yetkisini kontrol et
//        InvitedUser invitedUser = invitedUserRepository.findByEventAndId(event, authId)
//                .orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_UPDATE_PERMISSION_ERROR));
//
//       // event.setStatus(EStatus.ACTIVE); // Durumu aktif yap
//        eventRepository.save(event); // Güncellemeyi veritabanına kaydet
//
//        return true; // Başarılı onaylama işlemi
//    }

    private Long extractAuthIdFromToken(String token) {
        Optional<Long> authIdOptional = jwtTokenManager.getAuthIdFromToken(token);
        if (authIdOptional.isPresent()) {
            return authIdOptional.get();
        } else {
            throw new RuntimeException("AuthId could not be extracted from token");
        }
    }
}
