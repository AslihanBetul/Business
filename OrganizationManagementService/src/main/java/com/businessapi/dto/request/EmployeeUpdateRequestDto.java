package com.businessapi.dto.request;

public record EmployeeUpdateRequestDto(
        Long id,
        Long managerId,
        Long departmentId,
        String title,
        String identityNo,
        String phoneNo,
        String name,
        String surname

)
{
}
