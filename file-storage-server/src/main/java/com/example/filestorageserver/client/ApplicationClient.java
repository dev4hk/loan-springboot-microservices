package com.example.filestorageserver.client;

import com.example.filestorageserver.client.dto.ApplicationResponseDto;
import com.example.filestorageserver.client.fallback.ApplicationFallback;
import com.example.filestorageserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "application-server", fallback = ApplicationFallback.class)
public interface ApplicationClient {

    @GetMapping("/api/{applicationId}")
    ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId);

}
