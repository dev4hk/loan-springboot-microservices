package com.example.repayment_server.exception;

import com.example.repayment_server.dto.ResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class CustomFeignException extends RuntimeException {

    private HttpStatus httpStatus;
    private ResponseDTO<?> errorResponse;

    public CustomFeignException(HttpStatus status, ResponseDTO<?> errorResponse) {
        super("Feign client error: " + errorResponse.getResult().getDesc());
        this.httpStatus = status;
        this.errorResponse = errorResponse;
    }
}


