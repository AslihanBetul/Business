package com.bilgeadam.config.rabbit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmailVerificationModel {
    private String email;
    private String firstName;
    private String lastName;
}
