package com.berkhayta.analyticsservice.mapper;

import com.berkhayta.analyticsservice.dto.request.WidgetRequestDto;
import com.berkhayta.analyticsservice.dto.response.WidgetResponseDto;
import com.berkhayta.analyticsservice.entity.Widget;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WidgetMapper {

    Widget toEntity(WidgetRequestDto widgetRequestDto);

    WidgetResponseDto toDto(Widget widget);

    List<WidgetResponseDto> toDtoList(List<Widget> widgets);
}

