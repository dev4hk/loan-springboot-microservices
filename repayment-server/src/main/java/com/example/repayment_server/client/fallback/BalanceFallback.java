package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.ResponseDTO;
import com.example.repayment_server.dto.ResultObject;
import com.example.repayment_server.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BalanceFallback implements BalanceClient {
    private static final Logger logger = LoggerFactory.getLogger(BalanceFallback.class);

    @Override
    public ResponseDTO<List<BalanceResponseDto>> repaymentUpdate(Long applicationId, List<BalanceRepaymentRequestDto> request) {
        logger.error("BalanceFallback - repaymentUpdate invoked for applicationId: {}", applicationId);

        throw new BaseException(
                ResultType.SERVICE_UNAVAILABLE,
                "Balance service unavailable",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
