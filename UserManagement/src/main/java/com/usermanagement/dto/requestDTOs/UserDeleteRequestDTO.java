package com.usermanagement.dto.requestDTOs;

import com.usermanagement.constants.messages.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record UserDeleteRequestDTO(
        @NotBlank(message = ErrorMessages.USER_ID_CANT_BE_BLANK)
        Long userId
) {

}
