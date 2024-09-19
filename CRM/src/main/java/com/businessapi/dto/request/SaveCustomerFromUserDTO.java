package com.businessapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SaveCustomerFromUserDTO {

    private String firstName;
    private String lastName;
    private String email;
}
