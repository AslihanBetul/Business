package com.berkhayta.analyticsservice.controller;

import com.berkhayta.analyticsservice.dto.request.DashboardRequestDto;
import com.berkhayta.analyticsservice.dto.response.DashboardResponseDto;
import com.berkhayta.analyticsservice.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping
    public ResponseEntity<DashboardResponseDto> createDashboard(@RequestBody DashboardRequestDto dashboardRequestDto) {
        return ResponseEntity.ok(dashboardService.createDashboard(dashboardRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<DashboardResponseDto>> getAllDashboards() {
        return ResponseEntity.ok(dashboardService.getAllDashboards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardResponseDto> getDashboardById(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.getDashboardById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDashboard(@PathVariable Long id) {
        dashboardService.deleteDashboard(id);
        return ResponseEntity.noContent().build();
    }
}

