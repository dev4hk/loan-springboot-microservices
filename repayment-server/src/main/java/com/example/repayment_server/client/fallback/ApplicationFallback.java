package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.ResponseDTO;
import com.example.repayment_server.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFallback implements ApplicationClient {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationFallback.class);

    @Override
    public ResponseDTO<ApplicationResponseDto> get(Long applicationId) {
        logger.error("ApplicationFallback get() invoked for applicationId: {}", applicationId);

        ApplicationResponseDto fallbackResponse = ApplicationResponseDto.builder()
                .applicationId(applicationId)
                .build();
        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error fetching application")
                .build();

        return new ResponseDTO<>(resultObject, fallbackResponse);
    }
}
