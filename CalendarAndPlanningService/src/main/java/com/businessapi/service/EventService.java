package com.businessapi.service;

import com.businessapi.dto.request.EventDeleteRequestDTO;
import com.businessapi.dto.request.EventSaveRequestDTO;
import com.businessapi.dto.request.EventUpdateRequestDTO;
import com.businessapi.entity.Event;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.CalendarAndPlannigServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.events.EventException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final RabbitTemplate rabbitTemplate;

    public Boolean save(EventSaveRequestDTO eventSaveRequestDTO) {
        //Burada dto dan gelecek token ile user kontrol edilecek , user id gelecek

        Long userId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", eventSaveRequestDTO.token());

        Event event = Event.builder()
                .userId(userId)
                .title(eventSaveRequestDTO.title())
                .description(eventSaveRequestDTO.description())
                .location(eventSaveRequestDTO.location())
                .startDateTime(eventSaveRequestDTO.startDateTime())
                .endDateTime(eventSaveRequestDTO.endDateTime())
                .build();
        eventRepository.save(event);
        return true;
    }

    public Boolean update(EventUpdateRequestDTO eventUpdateRequestDTO) {
        Event event = eventRepository.findById(eventUpdateRequestDTO.id()).orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));

        Long userId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", eventUpdateRequestDTO.token());

        if (!event.getUserId().equals(userId)) {
            throw new CalendarAndPlannigServiceException(ErrorType.BAD_REQUEST_ERROR);
        }

        if (event.getStatus().equals(EStatus.ACTIVE)) {
            event.setTitle(eventUpdateRequestDTO.title() != null ? eventUpdateRequestDTO.title() : event.getTitle());
            event.setDescription(eventUpdateRequestDTO.description() != null ? eventUpdateRequestDTO.description() : event.getDescription());
            event.setLocation(eventUpdateRequestDTO.location() != null ? eventUpdateRequestDTO.location() : event.getLocation());
            event.setStartDateTime(eventUpdateRequestDTO.startDateTime() != null ? eventUpdateRequestDTO.startDateTime() : event.getStartDateTime());
            event.setEndDateTime(eventUpdateRequestDTO.endDateTime() != null ? eventUpdateRequestDTO.endDateTime() : event.getEndDateTime());
            eventRepository.save(event);
        }else
            throw new CalendarAndPlannigServiceException(ErrorType.EVENT_IS_NOT_ACTIVE);
        return true;

    }

    public Boolean delete(EventDeleteRequestDTO eventDeleteRequestDTO) {
        Event event = eventRepository.findById(eventDeleteRequestDTO.id()).orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));

        Long userId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", eventDeleteRequestDTO.token());

        if (!event.getUserId().equals(userId)) {
            throw new CalendarAndPlannigServiceException(ErrorType.BAD_REQUEST_ERROR);
        }

        if (event.getStatus().equals(EStatus.DELETED)) {
            throw new CalendarAndPlannigServiceException(ErrorType.BAD_REQUEST_ERROR);

        }
        event.setStatus(EStatus.DELETED);
        eventRepository.save(event);
        return true;
    }

    public List<Event> findAllByUserId(String token) {

        Long userId =(Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyGetUserIdByToken", token);

        return eventRepository.findAllByUserId(userId).orElseThrow(() -> new CalendarAndPlannigServiceException(ErrorType.EVENT_NOT_FOUND));
    }
}
