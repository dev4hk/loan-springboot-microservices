package com.example.balance_server.service.impl;

import com.example.balance_server.constants.ResultType;
import com.example.balance_server.dto.BalanceRepaymentRequestDto;
import com.example.balance_server.dto.BalanceRequestDto;
import com.example.balance_server.dto.BalanceResponseDto;
import com.example.balance_server.dto.BalanceUpdateRequestDto;
import com.example.balance_server.entity.Balance;
import com.example.balance_server.exception.BaseException;
import com.example.balance_server.mapper.BalanceMapper;
import com.example.balance_server.repository.BalanceRepository;
import com.example.balance_server.service.IBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements IBalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    public BalanceResponseDto create(Long applicationId, BalanceRequestDto request) {

        Balance balance = BalanceMapper.mapToBalance(request);

        balanceRepository.findByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(b.getUpdatedAt());
        });

        Balance created = balanceRepository.save(balance);

        return BalanceMapper.mapToBalanceResponseDto(created);
    }

    @Transactional(readOnly = true)
    @Override
    public BalanceResponseDto get(Long applicationId) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
        });
        return BalanceMapper.mapToBalanceResponseDto(balance);
    }

    @Override
    public BalanceResponseDto update(Long applicationId, BalanceUpdateRequestDto request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = balance.getBalance();

        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);
        balance.setBalance(updatedBalance);

        return BalanceMapper.mapToBalanceResponseDto(balance);
    }

    @Override
    public BalanceResponseDto repaymentUpdate(Long applicationId, BalanceRepaymentRequestDto request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
        });
        BigDecimal updatedBalance = balance.getBalance();
        BigDecimal repaymentAmount = request.getRepaymentAmount();
        if (request.getType().equals(BalanceRepaymentRequestDto.RepaymentType.ADD)) {
            updatedBalance = updatedBalance.add(repaymentAmount);
        } else {
            updatedBalance = updatedBalance.subtract(repaymentAmount);
        }
        balance.setBalance(updatedBalance);
        return BalanceMapper.mapToBalanceResponseDto(balance);
    }

    @Override
    public void delete(Long applicationId) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND));
        balance.setIsDeleted(true);
    }
}
