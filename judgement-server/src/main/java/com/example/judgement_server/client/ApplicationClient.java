package com.example.judgement_server.client;

import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.client.fallback.ApplicationClientFallbackFactory;
import com.example.judgement_server.config.FeignConfig;
import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "application-server", fallbackFactory = ApplicationClientFallbackFactory.class, configuration = FeignConfig.class)
public interface ApplicationClient {

    @GetMapping("/api/{applicationId}")
    ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId);

    @PutMapping("/api/{applicationId}/grant")
    ResponseDTO<Void> updateGrant(@PathVariable Long applicationId, @RequestBody GrantAmountDto grantAmountDto);

}
