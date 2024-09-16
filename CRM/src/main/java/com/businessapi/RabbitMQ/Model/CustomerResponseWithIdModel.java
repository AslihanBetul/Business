package com.businessapi.RabbitMQ.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerResponseWithIdModel {

    private String firstName;
    private String lastName;
    private Long customerId;
}
