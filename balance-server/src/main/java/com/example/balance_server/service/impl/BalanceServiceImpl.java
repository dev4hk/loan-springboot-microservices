package com.example.balance_server.service.impl;

import com.example.balance_server.constants.ResultType;
import com.example.balance_server.dto.BalanceRepaymentRequestDto;
import com.example.balance_server.dto.BalanceRepaymentRequestDto.RepaymentType;
import com.example.balance_server.dto.BalanceRequestDto;
import com.example.balance_server.dto.BalanceResponseDto;
import com.example.balance_server.dto.BalanceUpdateRequestDto;
import com.example.balance_server.entity.Balance;
import com.example.balance_server.exception.BaseException;
import com.example.balance_server.mapper.BalanceMapper;
import com.example.balance_server.repository.BalanceRepository;
import com.example.balance_server.service.IBalanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements IBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);
    private final BalanceRepository balanceRepository;

    @Override
    public BalanceResponseDto create(Long applicationId, BalanceRequestDto request) {
        logger.info("BalanceServiceImpl - create invoked");

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
        logger.info("BalanceServiceImpl - get invoked");
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            {
                logger.error("BalanceServiceImpl - Balance does not exist");
                return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Balance does not exist", HttpStatus.NOT_FOUND);
            }
        });
        return BalanceMapper.mapToBalanceResponseDto(balance);
    }

    @Override
    public BalanceResponseDto update(Long applicationId, BalanceUpdateRequestDto request) {
        logger.info("BalanceServiceImpl - update invoked");
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() ->
                {
                    logger.error("BalanceServiceImpl - Balance does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Balance does not exist", HttpStatus.NOT_FOUND);
                }
        );
        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = balance.getBalance();

        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);
        balance.setBalance(updatedBalance);

        return BalanceMapper.mapToBalanceResponseDto(balance);
    }

    @Override
    public List<BalanceResponseDto> repaymentUpdate(Long applicationId, List<BalanceRepaymentRequestDto> request) {
        logger.info("BalanceServiceImpl - repaymentUpdate invoked");
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            logger.error("BalanceServiceImpl - Balance does not exist");
            return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Balance does not exist", HttpStatus.NOT_FOUND);
        });

        BigDecimal currentBalance = balance.getBalance();

        List<Balance> responseList = new ArrayList<>();

        for (BalanceRepaymentRequestDto requestDto : request) {
            BigDecimal amount = requestDto.getRepaymentAmount();
            RepaymentType type = requestDto.getType();
            if (type.equals(RepaymentType.ADD)) {
                currentBalance = currentBalance.add(amount);
            } else {
                currentBalance = currentBalance.subtract(amount);
            }

            if (currentBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new BaseException(ResultType.BAD_REQUEST, "Repayment cannot be larger than current balance", HttpStatus.BAD_REQUEST);
            }

            balance.setBalance(currentBalance);
            Balance updatedEntity = balanceRepository.save(balance);
            responseList.add(updatedEntity);
        }
        return responseList.stream().map(BalanceMapper::mapToBalanceResponseDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long applicationId) {
        logger.info("BalanceServiceImpl - delete invoked");
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() ->
                {
                    logger.error("BalanceServiceImpl - Balance does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Balance does not exist", HttpStatus.NOT_FOUND);
                }
        );
        balance.setIsDeleted(true);
    }
}
