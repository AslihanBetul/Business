package com.businessapi.analyticsservice.service;

import com.businessapi.analyticsservice.dto.request.DashboardRequestDto;
import com.businessapi.analyticsservice.dto.response.DashboardResponseDto;
import com.businessapi.analyticsservice.entity.Dashboard;
import com.businessapi.analyticsservice.exception.AnalyticsServiceAppException;
import com.businessapi.analyticsservice.exception.ErrorType;
import com.businessapi.analyticsservice.mapper.DashboardMapper;
import com.businessapi.analyticsservice.repository.DashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final DashboardMapper dashboardMapper;

    public DashboardService(DashboardRepository dashboardRepository, DashboardMapper dashboardMapper) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardMapper = dashboardMapper;
    }

    public DashboardResponseDto createDashboard(DashboardRequestDto dashboardRequestDto) {
        Dashboard dashboard = dashboardMapper.toEntity(dashboardRequestDto);
        dashboard = dashboardRepository.save(dashboard);
        return dashboardMapper.toDto(dashboard);
    }

    public List<DashboardResponseDto> getAllDashboards() {
        return dashboardMapper.toDtoList(dashboardRepository.findAll());
    }

    public DashboardResponseDto getDashboardById(Long id) {
        Dashboard dashboard = dashboardRepository.findById(id)
                .orElseThrow(() -> new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "Dashboard not found for id: " + id));
        return dashboardMapper.toDto(dashboard);
    }

    public void deleteDashboard(Long id) {
        if (!dashboardRepository.existsById(id)) {
            throw new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "Dashboard not found for id: " + id);
        }
        dashboardRepository.deleteById(id);
    }
}
