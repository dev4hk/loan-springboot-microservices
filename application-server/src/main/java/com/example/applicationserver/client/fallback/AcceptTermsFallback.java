package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.CounselClient;
import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.AcceptTermsResponseDto;
import com.example.applicationserver.client.dto.CounselResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class AcceptTermsFallback implements AcceptTermsClient {

    private static final Logger logger = LoggerFactory.getLogger(AcceptTermsFallback.class);


    @Override
    public ResponseDTO<List<AcceptTermsResponseDto>> create(AcceptTermsRequestDto acceptTermsRequestDto) {
        logger.error("AcceptTermsFallback - create invoked");
        throw new BaseException(
                ResultType.SERVICE_UNAVAILABLE,
                "AcceptTerms service unavailable",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
