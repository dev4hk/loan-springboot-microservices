package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.TermsResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TermsFallback implements TermsClient {

    private static final Logger logger = LoggerFactory.getLogger(TermsFallback.class);

    @Override
    public ResponseDTO<List<TermsResponseDto>> getAll() {
        logger.error("TermsFallback getAll() invoked");

        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error fetching application")
                .build();

        return new ResponseDTO<>(resultObject, null);
    }
}
