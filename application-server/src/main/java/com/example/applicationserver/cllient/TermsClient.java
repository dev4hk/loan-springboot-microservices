package com.example.applicationserver.cllient;

import com.example.applicationserver.cllient.dto.TermsResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "terms", url = "http://localhost:8082")
public interface TermsClient {

    @GetMapping(value = "/terms")
    ResponseDTO<List<TermsResponseDto>> getAll();

}
