package com.businessapi.rabbitMQ.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerResponseModel {
    String firsName;
    String lastName;
    String email;
    String phone;

}
