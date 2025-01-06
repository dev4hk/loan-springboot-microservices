package com.example.repayment_server.exception;

import com.example.repayment_server.dto.ResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeignValidationException extends RuntimeException {

    private ResponseDTO<?> errorResponse;

    public FeignValidationException(ResponseDTO<?> errorResponse) {
        super(errorResponse.getResult().desc);
        this.errorResponse = errorResponse;
    }

}
