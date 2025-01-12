package com.example.entry_server.client.fallback;

import com.example.entry_server.client.BalanceClient;
import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceResponseDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.example.entry_server.constants.ResultType;
import com.example.entry_server.dto.ResponseDTO;
import com.example.entry_server.dto.ResultObject;
import com.example.entry_server.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BalanceFallback implements BalanceClient {

    private static final Logger logger = LoggerFactory.getLogger(BalanceFallback.class);

    @Override
    public ResponseDTO<BalanceResponseDto> create(Long applicationId, BalanceRequestDto request) {
        logger.error("BalanceFallback - create invoked for applicationId: {}", applicationId);

        throw new BaseException(
                ResultType.SERVICE_UNAVAILABLE,
                "Balance service unavailable",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @Override
    public ResponseDTO<BalanceResponseDto> update(Long applicationId, BalanceUpdateRequestDto request) {
        logger.error("BalanceFallback - update invoked for applicationId: {}", applicationId);

        throw new BaseException(
                ResultType.SERVICE_UNAVAILABLE,
                "Balance service unavailable",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
