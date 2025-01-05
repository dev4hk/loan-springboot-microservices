package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EntryFallback implements EntryClient {
    @Override
    public ResponseDTO<EntryResponseDto> getEntry(Long applicationId) {
        return null;
    }
}
