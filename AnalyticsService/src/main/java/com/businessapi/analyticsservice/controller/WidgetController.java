package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.dto.request.WidgetRequestDto;
import com.businessapi.analyticsservice.dto.response.WidgetResponseDto;
import com.businessapi.analyticsservice.dto.response.ResponseDTO;
import com.businessapi.analyticsservice.service.WidgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dev/v1/analytics/widgets")
public class WidgetController {

    private final WidgetService widgetService;

    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<WidgetResponseDto>> createWidget(@RequestBody WidgetRequestDto widgetRequestDto) {
        WidgetResponseDto widget = widgetService.createWidget(widgetRequestDto);
        return ResponseEntity.ok(ResponseDTO.<WidgetResponseDto>builder()
                .data(widget)
                .message("Success")
                .code(200)
                .build());
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<WidgetResponseDto>>> getAllWidgets() {
        List<WidgetResponseDto> widgets = widgetService.getAllWidgets();
        return ResponseEntity.ok(ResponseDTO.<List<WidgetResponseDto>>builder()
                .data(widgets)
                .message("Success")
                .code(200)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<WidgetResponseDto>> getWidgetById(@PathVariable Long id) {
        WidgetResponseDto widget = widgetService.getWidgetById(id);
        return ResponseEntity.ok(ResponseDTO.<WidgetResponseDto>builder()
                .data(widget)
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteWidget(@PathVariable Long id) {
        widgetService.deleteWidget(id);
        return ResponseEntity.ok(ResponseDTO.<Void>builder()
                .message("Success")
                .code(200)
                .build());
    }
}

