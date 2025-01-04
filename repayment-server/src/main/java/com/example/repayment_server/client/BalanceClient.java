package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "balance-server", url = "${client.balance.url}")
public interface BalanceClient {

    @PutMapping("/balances/{applicationId}/repayment")
    ResponseDTO<BalanceResponseDto> repaymentUpdate(@PathVariable Long applicationId, @RequestBody BalanceRepaymentRequestDto request);

}
