package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.AcceptTermsResponseDto;
import com.example.applicationserver.client.fallback.AcceptTermsFallback;
import com.example.applicationserver.config.FeignConfig;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "accept-terms-server", fallback = AcceptTermsFallback.class, configuration = FeignConfig.class)
public interface AcceptTermsClient {
    @PostMapping(value = "/api")
    ResponseDTO<List<AcceptTermsResponseDto>> create(@RequestBody AcceptTermsRequestDto acceptTermsRequestDto);
}
