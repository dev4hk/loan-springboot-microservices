package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.AcceptTermsResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ErrorResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.exception.BaseException;
import com.example.applicationserver.exception.CustomFeignException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AcceptTermsClientFallbackFactory implements FallbackFactory<AcceptTermsClient> {

    @Override
    public AcceptTermsClient create(Throwable cause) {
        return new AcceptTermsClient() {

            @Override
            public ResponseDTO<List<AcceptTermsResponseDto>> create(AcceptTermsRequestDto acceptTermsRequestDto) {
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

                throw new BaseException(ResultType.SERVICE_UNAVAILABLE, "AcceptTerms server unavailable", HttpStatus.SERVICE_UNAVAILABLE);

            }
        };
    }
}


