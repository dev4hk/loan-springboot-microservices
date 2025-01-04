package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.TermsResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "terms-server", url = "${client.terms.url}")
public interface TermsClient {

    @GetMapping(value = "/terms")
    ResponseDTO<List<TermsResponseDto>> getAll();

}
