package com.usermanagement.dto.requestDTOs;

import com.usermanagement.constants.messages.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoleCreateDTO {
    @Size(max = 60)
    @NotBlank(message = ErrorMessages.ROLE_CANT_BE_BLANK)
    private String roleName;
    @Size(max = 500)
    private String roleDescription;
}
