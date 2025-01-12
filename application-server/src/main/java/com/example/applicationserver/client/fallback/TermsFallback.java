package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.CounselClient;
import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.CounselResponseDto;
import com.example.applicationserver.client.dto.TermsResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TermsFallback implements TermsClient {

    private static final Logger logger = LoggerFactory.getLogger(TermsFallback.class);

    @Override
    public ResponseDTO<List<TermsResponseDto>> getAll() {
        logger.error("TermsFallback - getAll invoked");
        throw new BaseException(
                ResultType.SERVICE_UNAVAILABLE,
                "Terms service unavailable",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
