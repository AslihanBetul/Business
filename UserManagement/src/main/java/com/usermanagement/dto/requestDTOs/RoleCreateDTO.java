package com.usermanagement.dto.requestDTOs;

import com.usermanagement.constants.messages.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public record RoleCreateDTO (
        @Size(max = 60)
        @NotBlank(message = ErrorMessages.ROLE_CANT_BE_BLANK)
        String roleName,
        @Size(max = 500)
         String roleDescription){

}
