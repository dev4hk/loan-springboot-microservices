package com.example.applicationserver.exception;

import com.example.applicationserver.dto.ResponseDTO;
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
