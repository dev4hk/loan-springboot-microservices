package com.example.applicationserver.client;

import com.example.applicationserver.client.dto.JudgementResponseDto;
import com.example.applicationserver.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "judgement-server", url = "${client.judgement.url}")
public interface JudgementClient {

    @GetMapping("/applications/{applicationId}")
    ResponseDTO<JudgementResponseDto> getJudgmentOfApplication(@PathVariable Long applicationId);

}
