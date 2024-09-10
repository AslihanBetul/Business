package com.berkhayta.analyticsservice.mapper;

import com.berkhayta.analyticsservice.dto.request.KPIRequestDto;
import com.berkhayta.analyticsservice.dto.response.KPIResponseDto;
import com.berkhayta.analyticsservice.entity.KPI;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KPIMapper {
    KPIMapper INSTANCE = Mappers.getMapper(KPIMapper.class);

    KPI toEntity(KPIRequestDto dto);

    KPIResponseDto toDto(KPI entity);

    List<KPIResponseDto> toDtoList(List<KPI> entities);
}
