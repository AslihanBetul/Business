package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.dto.request.KPIRequestDto;
import com.businessapi.analyticsservice.dto.response.KPIResponseDto;
import com.businessapi.analyticsservice.dto.response.ResponseDTO;
import com.businessapi.analyticsservice.service.KPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev/v1/analytics/kpis")
public class KPIController {

    private final KPIService kpiService;

    public KPIController(KPIService kpiService) {
        this.kpiService = kpiService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<KPIResponseDto>> createKPI(@RequestBody KPIRequestDto kpiRequestDto) {
        KPIResponseDto responseDto = kpiService.createKPI(kpiRequestDto);
        return ResponseEntity.ok(ResponseDTO.<KPIResponseDto>builder()
                .data(responseDto)
                .message("Success")
                .code(200)
                .build());
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<KPIResponseDto>>> getAllKPIs() {
        List<KPIResponseDto> responseDtos = kpiService.getAllKPIs();
        return ResponseEntity.ok(ResponseDTO.<List<KPIResponseDto>>builder()
                .data(responseDtos)
                .message("Success")
                .code(200)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<KPIResponseDto>> getKPIById(@PathVariable Long id) {
        KPIResponseDto responseDto = kpiService.getKPIById(id);
        return ResponseEntity.ok(ResponseDTO.<KPIResponseDto>builder()
                .data(responseDto)
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteKPI(@PathVariable Long id) {
        kpiService.deleteKPI(id);
        return ResponseEntity.ok(ResponseDTO.<Void>builder()
                .message("Success")
                .code(200)
                .build());
    }

}