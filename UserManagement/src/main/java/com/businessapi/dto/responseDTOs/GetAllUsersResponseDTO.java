package com.businessapi.dto.responseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetAllUsersResponseDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private List<RoleResponseDTO> userRoles;
}
