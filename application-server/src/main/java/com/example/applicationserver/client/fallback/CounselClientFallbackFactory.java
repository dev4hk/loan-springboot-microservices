package com.example.applicationserver.client.fallback;

import com.example.applicationserver.client.CounselClient;
import com.example.applicationserver.client.dto.CounselResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CounselClientFallbackFactory implements FallbackFactory<CounselClient> {

    @Override
    public CounselClient create(Throwable cause) {
        return new CounselClient() {

            @Override
            public ResponseDTO<CounselResponseDto> getByEmail(String email) {
                return null;
            }
        };
    }
}


