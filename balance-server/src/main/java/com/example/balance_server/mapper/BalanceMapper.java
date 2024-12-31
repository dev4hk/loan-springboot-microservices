package com.example.balance_server.mapper;

import com.example.balance_server.dto.BalanceRequestDto;
import com.example.balance_server.dto.BalanceResponseDto;
import com.example.balance_server.entity.Balance;

public class BalanceMapper {

    public static Balance mapToBalance(BalanceRequestDto request) {
        return Balance.builder()
                .applicationId(request.getApplicationId())
                .balance(request.getEntryAmount())
                .build();
    }

    public static BalanceResponseDto mapToBalanceResponseDto(Balance balance) {
        return BalanceResponseDto.builder()
                .balanceId(balance.getBalanceId())
                .applicationId(balance.getApplicationId())
                .balance(balance.getBalance())
                .build();
    }
}
