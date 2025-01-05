package com.example.repayment_server.client.fallback;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFallback implements ApplicationClient {
    @Override
    public ResponseDTO<ApplicationResponseDto> get(Long applicationId) {
        return null;
    }
}
