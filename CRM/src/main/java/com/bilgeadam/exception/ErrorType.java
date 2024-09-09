package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorType {

    // Server errors
    INTERNAL_SERVER_ERROR(1000,"An unexpected error occurred on the server, please try again",HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_ERROR(1001,"An unexpected error occurred on the server, please try again",HttpStatus.INTERNAL_SERVER_ERROR ),
    BAD_REQUEST_ERROR(1002, "The information entered is incomplete or incorrect.",HttpStatus.BAD_REQUEST),

    // Token errors
    INVALID_TOKEN(2000, "Invalid token", HttpStatus.BAD_REQUEST),

    // Customer errors
    CUSTOMER_EMAIL_EXIST(3000,"The email already exists" , HttpStatus.BAD_REQUEST),
    NOT_FOUNDED_CUSTOMER(3001,"The customer not found" ,HttpStatus.NOT_FOUND ),
    CUSTOMER_NOT_ACTIVE(3002,"The customer may be inactive or deleted" ,HttpStatus.BAD_REQUEST );





    private Integer code;
    private String message;
    private HttpStatus httpStatus;
}
