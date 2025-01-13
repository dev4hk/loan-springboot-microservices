package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.dto.JudgementResponseDto;
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

@Component
public class JudgementClientFallbackFactory implements FallbackFactory<JudgementClient> {

    @Override
    public JudgementClient create(Throwable cause) {
        return new JudgementClient() {

            @Override
            public ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(Long applicationId) {
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

                throw new BaseException(ResultType.SERVICE_UNAVAILABLE, "Judgement server unavailable", HttpStatus.SERVICE_UNAVAILABLE);

            }
        };
    }
}


