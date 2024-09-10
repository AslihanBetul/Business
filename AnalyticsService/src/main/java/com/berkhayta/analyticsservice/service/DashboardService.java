package com.berkhayta.analyticsservice.service;

import com.berkhayta.analyticsservice.dto.request.DashboardRequestDto;
import com.berkhayta.analyticsservice.dto.response.DashboardResponseDto;
import com.berkhayta.analyticsservice.entity.Dashboard;
import com.berkhayta.analyticsservice.exception.AnalyticsServiceAppException;
import com.berkhayta.analyticsservice.exception.ErrorType;
import com.berkhayta.analyticsservice.mapper.DashboardMapper;
import com.berkhayta.analyticsservice.repository.DashboardRepository;
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
