package com.example.filestorageserver.client;

import com.example.filestorageserver.client.dto.ApplicationResponseDto;
import com.example.filestorageserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "application-server", url = "http://localhost:8081")
public interface ApplicationClient {

    @GetMapping(value = "/applications/{applicationId}")
    ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId);

}
