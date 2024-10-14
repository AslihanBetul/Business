package com.businessapi.dto.response;

public record EmployeeResponseDTO(
        Long id,
        String managerName,
        String departmentName,
        String identityNo,
        String phoneNo,
        String name,
        String surname,
        String email,
        Boolean isEmployeeTopLevelManager,
        Boolean isAccountGivenToEmployee
)
{
}
