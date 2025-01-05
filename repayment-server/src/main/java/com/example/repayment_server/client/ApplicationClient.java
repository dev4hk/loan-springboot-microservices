package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "application-server")
public interface ApplicationClient {

    @GetMapping("/api/{applicationId}")
    ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId);

}
