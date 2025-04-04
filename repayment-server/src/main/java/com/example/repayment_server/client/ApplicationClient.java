package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.client.fallback.ApplicationClientFallbackFactory;
import com.example.repayment_server.config.FeignConfig;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "application-server", fallbackFactory = ApplicationClientFallbackFactory.class, configuration = FeignConfig.class)
public interface ApplicationClient {

    @GetMapping("/api/{applicationId}")
    ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId);

    @PutMapping("/api/{applicationId}/complete")
    ResponseDTO<Void> complete(@PathVariable Long applicationId);


}
