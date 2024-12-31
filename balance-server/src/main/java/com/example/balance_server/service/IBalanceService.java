package com.example.balance_server.service;

import com.example.balance_server.dto.BalanceRepaymentRequestDto;
import com.example.balance_server.dto.BalanceRequestDto;
import com.example.balance_server.dto.BalanceResponseDto;
import com.example.balance_server.dto.BalanceUpdateRequestDto;

public interface IBalanceService {
    BalanceResponseDto create(Long applicationId, BalanceRequestDto request);
    BalanceResponseDto get(Long applicationId);
    BalanceResponseDto update(Long applicationId, BalanceUpdateRequestDto request);
    BalanceResponseDto repaymentUpdate(Long applicationId, BalanceRepaymentRequestDto request);
    void delete(Long applicationId);
}
