package com.businessapi.RabbitMQ.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQNotification implements Serializable {
    private Long userId;
    private String message;
}

