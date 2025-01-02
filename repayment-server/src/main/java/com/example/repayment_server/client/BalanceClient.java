package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "balance-server", url = "${client.balance.url}")
public interface BalanceClient {

    @PatchMapping("/{applicationId}/repayment")
    ResponseDTO<BalanceResponseDto> repaymentUpdate(@PathVariable Long applicationId, @Valid @RequestBody BalanceRepaymentRequestDto request);

}
