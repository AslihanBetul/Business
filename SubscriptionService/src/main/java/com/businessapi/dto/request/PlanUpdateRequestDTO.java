package com.businessapi.dto.request;

import com.businessapi.entity.enums.ERoles;

import java.util.List;

public record PlanUpdateRequestDTO(
    Long id,
    String name,
    Double price,
    List<ERoles> roles
){
}
