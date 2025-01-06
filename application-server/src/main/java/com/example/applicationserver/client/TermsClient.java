package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.TermsResponseDto;
import com.example.applicationserver.client.fallback.JudgementFallback;
import com.example.applicationserver.client.fallback.TermsFallback;
import com.example.applicationserver.config.FeignConfig;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "terms-server", fallback = TermsFallback.class, configuration = FeignConfig.class)
public interface TermsClient {

    @GetMapping(value = "/api")
    ResponseDTO<List<TermsResponseDto>> getAll();

}
