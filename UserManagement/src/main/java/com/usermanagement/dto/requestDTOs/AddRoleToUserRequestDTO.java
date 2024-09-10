package com.usermanagement.dto.requestDTOs;

public record AddRoleToUserRequestDTO(
        Long userId,
        Long roleId
) {
}
