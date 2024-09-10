package com.usermanagement.RabbitMQ.Model;

import com.usermanagement.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerSaveFromUserModel {
    private Long authId;
    private Long userId;
    private EStatus status;

}
