package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.client.fallback.EntryClientFallbackFactory;
import com.example.repayment_server.config.FeignConfig;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "entry-server", fallbackFactory = EntryClientFallbackFactory.class, configuration = FeignConfig.class)
public interface EntryClient {

    @GetMapping("/api/{applicationId}")
    ResponseDTO<EntryResponseDto> getEntry(@PathVariable Long applicationId);

}
