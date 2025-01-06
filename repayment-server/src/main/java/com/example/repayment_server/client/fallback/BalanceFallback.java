package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.ResponseDTO;
import com.example.repayment_server.dto.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BalanceFallback implements BalanceClient {
    private static final Logger logger = LoggerFactory.getLogger(BalanceFallback.class);

    @Override
    public ResponseDTO<BalanceResponseDto> repaymentUpdate(Long applicationId, BalanceRepaymentRequestDto request) {
        logger.error("BalanceFallback repaymentUpdate() invoked for applicationId: {}", applicationId);

        BalanceResponseDto fallbackResponseDto = BalanceResponseDto.builder()
                .applicationId(applicationId)
                .build();
        ResultObject resultObject = ResultObject.builder()
                .code(ResultType.SYSTEM_ERROR.getCode())
                .desc("Error updating repayment")
                .build();

        return new ResponseDTO<>(resultObject, fallbackResponseDto);
    }
}
