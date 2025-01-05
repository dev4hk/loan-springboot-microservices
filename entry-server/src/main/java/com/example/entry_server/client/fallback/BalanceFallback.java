package com.example.entry_server.client.fallback;

import com.example.entry_server.client.BalanceClient;
import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceResponseDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.example.entry_server.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class BalanceFallback implements BalanceClient {
    @Override
    public ResponseDTO<BalanceResponseDto> create(Long applicationId, BalanceRequestDto request) {
        return null;
    }

    @Override
    public ResponseDTO<BalanceResponseDto> update(Long applicationId, BalanceUpdateRequestDto request) {
        return null;
    }
}
