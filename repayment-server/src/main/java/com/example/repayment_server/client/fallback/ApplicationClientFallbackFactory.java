package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.ErrorResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import com.example.repayment_server.exception.BaseException;
import com.example.repayment_server.exception.CustomFeignException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApplicationClientFallbackFactory implements FallbackFactory<ApplicationClient> {
    @Override
    public ApplicationClient create(Throwable cause) {
        return new ApplicationClient() {
            @Override
            public ResponseDTO<ApplicationResponseDto> get(Long applicationId) {
                if (cause instanceof FeignException) {
                    FeignException feignException = (FeignException) cause;
                    HttpStatus status = HttpStatus.valueOf(feignException.status());
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());

                    try {
                        String responseBody = feignException.contentUTF8();
                        TypeReference<ResponseDTO<ErrorResponseDto>> typeReference = new TypeReference<ResponseDTO<ErrorResponseDto>>() {
                        };
                        ResponseDTO<ErrorResponseDto> errorResponseDto = objectMapper.readValue(responseBody, typeReference);
                        throw new CustomFeignException(status, errorResponseDto);
                    } catch (IOException e) {
                        throw new CustomFeignException(HttpStatus.INTERNAL_SERVER_ERROR, null);
                    }
                }

                throw new BaseException(ResultType.SERVICE_UNAVAILABLE, "Application server unavailable", HttpStatus.SERVICE_UNAVAILABLE);

            }

            @Override
            public ResponseDTO<Void> complete(Long applicationId) {
                if (cause instanceof FeignException) {
                    FeignException feignException = (FeignException) cause;
                    HttpStatus status = HttpStatus.valueOf(feignException.status());
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());

                    try {
                        String responseBody = feignException.contentUTF8();
                        TypeReference<ResponseDTO<ErrorResponseDto>> typeReference = new TypeReference<ResponseDTO<ErrorResponseDto>>() {
                        };
                        ResponseDTO<ErrorResponseDto> errorResponseDto = objectMapper.readValue(responseBody, typeReference);
                        throw new CustomFeignException(status, errorResponseDto);
                    } catch (IOException e) {
                        throw new CustomFeignException(HttpStatus.INTERNAL_SERVER_ERROR, null);
                    }
                }

                throw new BaseException(ResultType.SERVICE_UNAVAILABLE, "Application server unavailable", HttpStatus.SERVICE_UNAVAILABLE);

            }
        };
    }
}
