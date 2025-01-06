package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.ResponseDTO;
import com.example.repayment_server.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EntryFallback implements EntryClient {
    private static final Logger logger = LoggerFactory.getLogger(EntryFallback.class);

    @Override
    public ResponseDTO<EntryResponseDto> getEntry(Long applicationId) {
        logger.error("EntryFallback getEntry() invoked for applicationId: {}", applicationId);

        EntryResponseDto fallbackResponseDto = EntryResponseDto.builder()
                .applicationId(applicationId)
                .build();
        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error fetching entry")
                .build();

        return new ResponseDTO<>(resultObject, fallbackResponseDto);
    }
}
