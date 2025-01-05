package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "entry-server", url = "${client.entry.url}")
public interface EntryClient {

    @GetMapping("/api/{applicationId}")
    ResponseDTO<EntryResponseDto> getEntry(@PathVariable Long applicationId);

}
