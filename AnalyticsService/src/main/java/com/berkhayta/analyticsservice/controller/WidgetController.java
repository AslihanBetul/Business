package com.berkhayta.analyticsservice.controller;

import com.berkhayta.analyticsservice.dto.request.WidgetRequestDto;
import com.berkhayta.analyticsservice.dto.response.WidgetResponseDto;
import com.berkhayta.analyticsservice.service.WidgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/widgets")
public class WidgetController {

    private final WidgetService widgetService;

    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    public ResponseEntity<WidgetResponseDto> createWidget(@RequestBody WidgetRequestDto widgetRequestDto) {
        return ResponseEntity.ok(widgetService.createWidget(widgetRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<WidgetResponseDto>> getAllWidgets() {
        return ResponseEntity.ok(widgetService.getAllWidgets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WidgetResponseDto> getWidgetById(@PathVariable Long id) {
        return ResponseEntity.ok(widgetService.getWidgetById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWidget(@PathVariable Long id) {
        widgetService.deleteWidget(id);
        return ResponseEntity.noContent().build();
    }
}

