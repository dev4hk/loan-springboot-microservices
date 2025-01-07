package com.example.repayment_server.client;

import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.client.fallback.BalanceFallback;
import com.example.repayment_server.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "balance-server", fallback = BalanceFallback.class)
public interface BalanceClient {

    @PutMapping("/api/{applicationId}/repayment")
    ResponseDTO<List<BalanceResponseDto>> repaymentUpdate(@PathVariable Long applicationId, @RequestBody List<BalanceRepaymentRequestDto> request);

}
