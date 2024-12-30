package com.example.judgement_server.client;

import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "application-server", url = "http://localhost:8081")
public interface ApplicationClient {

    @GetMapping("/applications/{applicationId}")
    ResponseDTO<ApplicationResponseDto> get(@PathVariable Long applicationId);

}
