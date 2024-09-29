package com.businessapi.controller;

import com.businessapi.constants.EndPoints;
import com.businessapi.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(EndPoints.EVENT)
public class EventController {
    private final EventService eventService;
}
