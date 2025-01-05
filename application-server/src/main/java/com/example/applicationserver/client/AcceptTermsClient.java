package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.client.dto.AcceptTermsResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "accept-terms-server", url = "${client.accept-terms.url}")
public interface AcceptTermsClient {
    @PostMapping(value = "/api")
    ResponseDTO<List<AcceptTermsResponseDto>> create(@RequestBody AcceptTermsRequestDto acceptTermsRequestDto);
}
