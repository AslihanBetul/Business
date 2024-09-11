package com.bilgeadam.config.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserSaveFromAuthModel {
    private Long authId;
    private String firstName;
    private String lastName;
}
