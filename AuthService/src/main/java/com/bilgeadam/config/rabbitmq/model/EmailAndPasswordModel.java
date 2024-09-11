package com.bilgeadam.config.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmailAndPasswordModel {
    private String email;
    private String encryptedPassword;
}