package com.bilgeadam.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    @Email
    @Column(unique = true)
    private String email;
    @Size(min = 4,max = 32, message = "Password must be between 4 and 32.")
    private String password;
    @Size(min = 4,max = 32, message = "Password must be between 4 and 32.")
    private String rePassword;
}
