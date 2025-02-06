package com.example.entry_server.client;

import com.example.entry_server.client.dto.JudgementResponseDto;
import com.example.entry_server.client.fallback.JudgementClientFallbackFactory;
import com.example.entry_server.config.FeignConfig;
import com.example.entry_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "judgement-server", fallbackFactory = JudgementClientFallbackFactory.class, configuration = FeignConfig.class)
public interface JudgementClient {

    @GetMapping("/api/applications/{applicationId}")
    ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(@PathVariable Long applicationId);
}
