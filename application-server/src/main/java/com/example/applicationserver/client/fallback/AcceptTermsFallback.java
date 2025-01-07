package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.AcceptTermsResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AcceptTermsFallback implements AcceptTermsClient {

    private static final Logger logger = LoggerFactory.getLogger(AcceptTermsFallback.class);

    @Override
    public ResponseDTO<List<AcceptTermsResponseDto>> create(AcceptTermsRequestDto acceptTermsRequestDto) {
        logger.error("AcceptTermsFallback create() invoked");
        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error creating accept terms")
                .build();

        return new ResponseDTO<>(resultObject, null);
    }
}
