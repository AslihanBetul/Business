package com.usermanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorType {

    BAD_REQUEST_ERROR(1001, "Girilen bilgiler eksik ya da yanlıştır.",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1002,"Token Geçersiz", HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR(9001,"Sunucuda beklenmeyen bir hata oluştu, Lütfen tekrar deneyiniz",HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR_NOT_FOUND_DATA(9002,"Sunucu Hatası: Liste getirilemedi, lütfen tekrar deneyin", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(2001,"User not found", HttpStatus.NOT_FOUND),

    ROLE_DATA_IS_EMPTY(2002,"Role data is empty", HttpStatus.NOT_FOUND),

    ROLE_NOT_FOUND(2003,"Role not found", HttpStatus.NOT_FOUND),


    ;







    private Integer code;
    private String message;
    private HttpStatus httpStatus;
}
