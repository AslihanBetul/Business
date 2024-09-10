package com.berkhayta.analyticsservice.controller;

import com.berkhayta.analyticsservice.dto.request.KPIRequestDto;
import com.berkhayta.analyticsservice.dto.response.KPIResponseDto;
import com.berkhayta.analyticsservice.service.KPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kpis")
public class KPIController {

    private final KPIService kpiService;

    public KPIController(KPIService kpiService) {
        this.kpiService = kpiService;
    }

    @PostMapping
    public ResponseEntity<KPIResponseDto> createKPI(@RequestBody KPIRequestDto kpiRequestDto) {
        KPIResponseDto responseDto = kpiService.createKPI(kpiRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<KPIResponseDto>> getAllKPIs() {
        List<KPIResponseDto> responseDtos = kpiService.getAllKPIs();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KPIResponseDto> getKPIById(@PathVariable Long id) {
        KPIResponseDto responseDto = kpiService.getKPIById(id);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKPI(@PathVariable Long id) {
        kpiService.deleteKPI(id);
        return ResponseEntity.noContent().build();
    }
}