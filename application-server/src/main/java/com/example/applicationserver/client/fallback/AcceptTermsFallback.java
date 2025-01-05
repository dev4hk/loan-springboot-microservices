package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.AcceptTermsResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AcceptTermsFallback implements AcceptTermsClient {


    @Override
    public ResponseDTO<List<AcceptTermsResponseDto>> create(AcceptTermsRequestDto acceptTermsRequestDto) {
        return null;
    }
}
