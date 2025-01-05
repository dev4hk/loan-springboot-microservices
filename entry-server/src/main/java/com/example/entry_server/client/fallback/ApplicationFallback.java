package com.example.entry_server.client.fallback;

import com.example.entry_server.client.ApplicationClient;
import com.example.entry_server.client.dto.ApplicationResponseDto;
import com.example.entry_server.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFallback implements ApplicationClient {
    @Override
    public ResponseDTO<ApplicationResponseDto> get(Long applicationId) {
        return null;
    }
}
