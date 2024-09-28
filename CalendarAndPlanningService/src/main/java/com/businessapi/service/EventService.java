package com.businessapi.service;

import com.businessapi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
}
