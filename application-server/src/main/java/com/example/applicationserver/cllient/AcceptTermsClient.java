package com.example.applicationserver.cllient;

import com.example.applicationserver.cllient.dto.AcceptTermsRequestDto;
import com.example.applicationserver.cllient.dto.AcceptTermsResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "accept-terms", url = "http://localhost:8083")
public interface AcceptTermsClient {
    @PostMapping(value = "/accept-terms")
    ResponseDTO<List<AcceptTermsResponseDto>> create(@RequestBody AcceptTermsRequestDto acceptTermsRequestDto);
}
