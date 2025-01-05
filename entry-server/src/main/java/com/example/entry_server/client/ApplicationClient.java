package com.example.entry_server.client;

import com.example.entry_server.client.dto.ApplicationResponseDto;
import com.example.entry_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "application-server", url = "${client.application.url}")
public interface ApplicationClient {

    @GetMapping("/api/{applicationId}")
    ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId);

}
