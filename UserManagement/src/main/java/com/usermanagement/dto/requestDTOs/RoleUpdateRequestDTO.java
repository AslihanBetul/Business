package com.usermanagement.dto.requestDTOs;

import com.usermanagement.constants.messages.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record RoleUpdateRequestDTO(
        @NotBlank(message = ErrorMessages.ROLE_ID_CANT_BE_BLANK)
        Long roleId,
        @Size(max = 60)
        @NotBlank(message = ErrorMessages.ROLE_CANT_BE_BLANK)
        String roleName,
        @Size(max = 500)
        String roleDescription
) {

}
