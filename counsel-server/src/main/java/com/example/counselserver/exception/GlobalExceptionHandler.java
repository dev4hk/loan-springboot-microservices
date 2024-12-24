package com.example.counselserver.exception;

import com.example.counselserver.dto.ErrorResponseDto;
import com.example.counselserver.dto.ResponseDTO;
import com.example.counselserver.dto.ResultObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException exception, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(exception.getDesc())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(ResponseDTO.builder()
                .result(new ResultObject(exception))
                .data(errorResponseDto)
                .build(), HttpStatus.BAD_REQUEST);
    }
}
