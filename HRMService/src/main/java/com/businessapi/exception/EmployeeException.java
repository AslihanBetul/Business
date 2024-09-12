package com.businessapi.exception;

import lombok.Getter;


@Getter
public class EmployeeException extends RuntimeException{
     private ErrorType errorType;
    public EmployeeException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
