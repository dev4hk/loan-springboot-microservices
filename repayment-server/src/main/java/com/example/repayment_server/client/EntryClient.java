package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "entry-server", url = "${client.entry.url}")
@FeignClient("entry-server")
public interface EntryClient {

    @GetMapping("/entries/{applicationId}")
    ResponseDTO<EntryResponseDto> getEntry(@PathVariable Long applicationId);

}
