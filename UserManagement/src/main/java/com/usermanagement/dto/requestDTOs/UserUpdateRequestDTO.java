package com.usermanagement.dto.requestDTOs;

import com.usermanagement.constants.messages.ErrorMessages;
import jakarta.validation.constraints.Email;
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
public class UserUpdateRequestDTO {
    @NotBlank(message = ErrorMessages.AUTH_ID_CANT_BE_BLANK)
    private Long authId;
    @Size(max = 40, message = ErrorMessages.FIRST_NAME_CANT_EXCEED_LENGTH)
    @NotBlank(message = ErrorMessages.FIRST_NAME_CANT_BE_BLANK)
    private String firstName;
    @Size(message = ErrorMessages.LAST_NAME_EXCEED_LENGTH)
    @NotBlank(message = ErrorMessages.LAST_NAME_CANT_BE_BLANK)
    private String lastName;
    @Email(message = ErrorMessages.EMAIL_TYPE_IS_WRONG)
    private String email; //TODO Bu auth servise gönderilecek
}
