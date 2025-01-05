package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.dto.JudgementResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class JudgementFallback implements JudgementClient {
    @Override
    public ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(Long applicationId) {
        return null;
    }
}
