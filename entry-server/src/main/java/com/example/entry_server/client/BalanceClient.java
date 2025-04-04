package com.example.entry_server.client;

import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceResponseDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.example.entry_server.client.fallback.BalanceClientFallbackFactory;
import com.example.entry_server.config.FeignConfig;
import com.example.entry_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "balance-server", fallbackFactory = BalanceClientFallbackFactory.class, configuration = FeignConfig.class)
public interface BalanceClient {

    @PostMapping("/api/{applicationId}")
    ResponseDTO<BalanceResponseDto> create(@PathVariable Long applicationId, @RequestBody BalanceRequestDto request);

    @PutMapping("/api/{applicationId}")
    ResponseDTO<BalanceResponseDto> update(@PathVariable Long applicationId, @RequestBody BalanceUpdateRequestDto request);

}
