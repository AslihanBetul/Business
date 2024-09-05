package com.usermanagement.dto.requestDTOs;

import com.usermanagement.constants.messages.ErrorMessages;
import com.usermanagement.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserSaveRequestDTO {
    @NotBlank(message = ErrorMessages.AUTH_ID_CANT_BE_BLANK)
    private Long authId;
    @Size(max = 40, message = ErrorMessages.FIRST_NAME_CANT_EXCEED_LENGTH)
    @NotBlank(message = ErrorMessages.FIRST_NAME_CANT_BE_BLANK)
    private String firstName;
    @Size(message = ErrorMessages.LAST_NAME_EXCEED_LENGTH)
    @NotBlank(message = ErrorMessages.LAST_NAME_CANT_BE_BLANK)
    private String lastName;
    @NotEmpty(message = ErrorMessages.ROLE_LIST_CANT_BE_EMPTY)
    private List<Long> roleIds;
}
