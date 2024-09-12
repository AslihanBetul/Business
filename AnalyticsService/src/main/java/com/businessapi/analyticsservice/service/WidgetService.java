package com.businessapi.analyticsservice.service;

import com.businessapi.analyticsservice.dto.request.WidgetRequestDto;
import com.businessapi.analyticsservice.dto.response.WidgetResponseDto;
import com.businessapi.analyticsservice.entity.Widget;
import com.businessapi.analyticsservice.exception.AnalyticsServiceAppException;
import com.businessapi.analyticsservice.exception.ErrorType;
import com.businessapi.analyticsservice.mapper.WidgetMapper;
import com.businessapi.analyticsservice.repository.WidgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class WidgetService {

    private final WidgetRepository widgetRepository;
    private final WidgetMapper widgetMapper;

    public WidgetService(WidgetRepository widgetRepository, WidgetMapper widgetMapper) {
        this.widgetRepository = widgetRepository;
        this.widgetMapper = widgetMapper;
    }

    public WidgetResponseDto createWidget(WidgetRequestDto widgetRequestDto) {
        Widget widget = widgetMapper.toEntity(widgetRequestDto);
        widget = widgetRepository.save(widget);
        return widgetMapper.toDto(widget);
    }

    public List<WidgetResponseDto> getAllWidgets() {
        return widgetMapper.toDtoList(widgetRepository.findAll());
    }

    public WidgetResponseDto getWidgetById(Long id) {
        Widget widget = widgetRepository.findById(id)
                .orElseThrow(() -> new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "Widget not found for id: " + id));
        return widgetMapper.toDto(widget);
    }

    public void deleteWidget(Long id) {
        if (!widgetRepository.existsById(id)) {
            throw new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "Widget not found for id: " + id);
        }
        widgetRepository.deleteById(id);
    }
}
