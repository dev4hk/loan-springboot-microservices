package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class BalanceFallback implements BalanceClient {
    @Override
    public ResponseDTO<BalanceResponseDto> repaymentUpdate(Long applicationId, BalanceRepaymentRequestDto request) {
        return null;
    }
}
