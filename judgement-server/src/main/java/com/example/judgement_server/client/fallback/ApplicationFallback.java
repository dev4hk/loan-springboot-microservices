package com.example.judgement_server.client.fallback;

import com.example.judgement_server.client.ApplicationClient;
import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFallback implements ApplicationClient {
    @Override
    public ResponseDTO<ApplicationResponseDto> get(Long applicationId) {
        return null;
    }

    @Override
    public ResponseDTO<Void> updateGrant(Long applicationId, GrantAmountDto grantAmountDto) {
        return null;
    }
}
