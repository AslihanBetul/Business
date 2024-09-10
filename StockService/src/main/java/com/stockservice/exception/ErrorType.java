package com.stockservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorType {


    INTERNAL_SERVER_ERROR(9000,"Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),

    BAD_REQUEST_ERROR(9001,"Bad Request",   HttpStatus.BAD_REQUEST ),
    WAREHOUSE_NOT_FOUND(9002,"Ware House Not Found" ,HttpStatus.BAD_REQUEST ),
    PRODUCT_NOT_FOUND(9003,"Product Not Found", HttpStatus.BAD_REQUEST  ),
    INSUFFICIENT_STOCK( 9004, "Insufficient Stock",  HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_ACTIVE( 9005,  "Product Not Active" , HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(9006,"Order Not Found" ,    HttpStatus.BAD_REQUEST),
    PRODUCT_CATEGORY_NOT_FOUND(9007,    "Product Category Not Found" , HttpStatus.BAD_REQUEST),
    STOCK_MOVEMENT_NOT_FOUND(9008, "Stock Movement Not Found", HttpStatus.BAD_REQUEST),
    SUPPLIER_NOT_FOUND( 9009,  "Supplier Not Found" ,  HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(9010,"Invalid Token" ,    HttpStatus.BAD_REQUEST );

    private Integer code;
    private String message;
    private HttpStatus httpStatus;
}
