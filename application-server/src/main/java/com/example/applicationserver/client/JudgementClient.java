package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.JudgementResponseDto;
import com.example.applicationserver.client.fallback.JudgementClientFallbackFactory;
import com.example.applicationserver.config.FeignConfig;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "judgement-server", fallbackFactory = JudgementClientFallbackFactory.class, configuration = FeignConfig.class)
public interface JudgementClient {

    @GetMapping("/api/applications/{applicationId}")
    ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(@PathVariable Long applicationId);

}
