package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.dto.request.DashboardRequestDto;
import com.businessapi.analyticsservice.dto.response.DashboardResponseDto;
import com.businessapi.analyticsservice.dto.response.ResponseDTO;
import com.businessapi.analyticsservice.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dev/v1/analytics/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<DashboardResponseDto>> createDashboard(@RequestBody DashboardRequestDto dashboardRequestDto) {
        return ResponseEntity.ok(
                ResponseDTO.<DashboardResponseDto>builder()
                        .data(dashboardService.createDashboard(dashboardRequestDto))
                        .message("Dashboard created successfully")
                        .code(200)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<DashboardResponseDto>>> getAllDashboards() {
        return ResponseEntity.ok(
                ResponseDTO.<List<DashboardResponseDto>>builder()
                        .data(dashboardService.getAllDashboards())
                        .message("All dashboards fetched successfully")
                        .code(200)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DashboardResponseDto>> getDashboardById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseDTO.<DashboardResponseDto>builder()
                        .data(dashboardService.getDashboardById(id))
                        .message("Dashboard fetched successfully")
                        .code(200)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Boolean>> deleteDashboard(@PathVariable Long id) {
        dashboardService.deleteDashboard(id);
        return ResponseEntity.ok(
                ResponseDTO.<Boolean>builder()
                        .data(true)
                        .message("Dashboard deleted successfully")
                        .code(200)
                        .build()
        );
    }
}

