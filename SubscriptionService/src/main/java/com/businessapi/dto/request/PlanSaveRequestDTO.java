package com.businessapi.dto.request;

import com.businessapi.entity.enums.ERoles;

import java.util.List;

public record PlanSaveRequestDTO (
    String name,
    Double price,
    List<ERoles> roles
){
}
