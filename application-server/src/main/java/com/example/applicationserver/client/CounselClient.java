package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.CounselResponseDto;
import com.example.applicationserver.client.fallback.CounselFallback;
import com.example.applicationserver.config.FeignConfig;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "counsel-server", fallback = CounselFallback.class, configuration = FeignConfig.class)
public interface CounselClient {
    @PostMapping(value = "/api/email")
    public ResponseDTO<CounselResponseDto> getByEmail(@RequestParam String email);
}

