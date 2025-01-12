package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.CounselClient;
import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.AcceptTermsResponseDto;
import com.example.applicationserver.client.dto.CounselResponseDto;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
public class CounselFallback implements CounselClient {

    private static final Logger logger = LoggerFactory.getLogger(CounselFallback.class);

    @Override
    public ResponseDTO<CounselResponseDto> getByEmail(String email) {
        logger.error("CounselFallback - getByEmail invoked");
        return null;
    }
}
