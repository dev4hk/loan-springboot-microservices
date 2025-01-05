package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.TermsResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TermsFallback implements TermsClient {
    @Override
    public ResponseDTO<List<TermsResponseDto>> getAll() {
        return null;
    }
}
