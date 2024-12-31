package com.example.balance_server.controller;

import com.example.balance_server.dto.*;
import com.example.balance_server.service.IBalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final IBalanceService balanceService;

    @PostMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> create(@PathVariable Long applicationId, @Valid @RequestBody BalanceRequestDto request) {
        BalanceResponseDto response = balanceService.create(applicationId, request);
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> get(@PathVariable Long applicationId) {
        BalanceResponseDto response = balanceService.get(applicationId);
        return ResponseDTO.ok(response);
    }

    @PutMapping("/{applicationId}")
    public ResponseDTO<BalanceResponseDto> update(@PathVariable Long applicationId, @Valid @RequestBody BalanceUpdateRequestDto request) {
        BalanceResponseDto response = balanceService.update(applicationId, request);
        return ResponseDTO.ok(response);
    }

    @PatchMapping("/{applicationId}/repayment")
    public ResponseDTO<BalanceResponseDto> repaymentUpdate(@PathVariable Long applicationId, @Valid @RequestBody BalanceRepaymentRequestDto request) {
        BalanceResponseDto response = balanceService.repaymentUpdate(applicationId, request);
        return ResponseDTO.ok(response);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> delete(@PathVariable Long applicationId) {
        balanceService.delete(applicationId);
        return ResponseDTO.ok();
    }
}
