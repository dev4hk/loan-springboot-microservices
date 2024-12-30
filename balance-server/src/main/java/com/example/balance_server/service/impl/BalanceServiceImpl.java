package com.example.balance_server.service.impl;

import com.example.balance_server.dto.BalanceRepaymentRequestDto;
import com.example.balance_server.dto.BalanceRequestDto;
import com.example.balance_server.dto.BalanceResponseDto;
import com.example.balance_server.dto.BalanceUpdateRequestDto;
import com.example.balance_server.repository.BalanceRepository;
import com.example.balance_server.service.IBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements IBalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    public BalanceResponseDto create(Long applicationId, BalanceRequestDto request) {
        return null;
    }

    @Override
    public BalanceResponseDto get(Long applicationId) {
        return null;
    }

    @Override
    public BalanceResponseDto update(Long applicationId, BalanceUpdateRequestDto request) {
        return null;
    }

    @Override
    public BalanceResponseDto repaymentUpdate(Long applicationId, BalanceRepaymentRequestDto request) {
        return null;
    }

    @Override
    public void delete(Long applicationId) {

    }
}
