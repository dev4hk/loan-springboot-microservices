package com.example.repayment_server.service.impl;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.*;
import com.example.repayment_server.entity.Repayment;
import com.example.repayment_server.exception.BaseException;
import com.example.repayment_server.mapper.RepaymentMapper;
import com.example.repayment_server.repository.RepaymentRepository;
import com.example.repayment_server.service.IRepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class RepaymentServiceImpl implements IRepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final ApplicationClient applicationClient;
    private final BalanceClient balanceClient;
    private final EntryClient entryClient;

    @Override
    public RepaymentResponseDto create(Long applicationId, RepaymentRequestDto repaymentRequestDto) {
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.BAD_REQUEST, "Application not repayable", HttpStatus.BAD_REQUEST);
        }

        Repayment repayment = RepaymentMapper.mapToRepayment(repaymentRequestDto);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);

        ResponseDTO<BalanceResponseDto> balanceResponseDto = balanceClient.repaymentUpdate(
                applicationId,
                BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(repaymentRequestDto.getRepaymentAmount())
                        .type(BalanceRepaymentRequestDto.RepaymentType.REMOVE)
                        .build()
        );

        RepaymentResponseDto repaymentResponseDto = RepaymentMapper.mapToRepaymentResponseDto(repayment);
        repaymentResponseDto.setBalance(balanceResponseDto.getData().getBalance());

        return repaymentResponseDto;
    }

    private boolean isRepayableApplication(Long applicationId) {
        ResponseDTO<ApplicationResponseDto> applicationResponseDto = applicationClient.get(applicationId);
        if (
                applicationResponseDto == null
                        || applicationResponseDto.getData() == null
                        || applicationResponseDto.getData().getContractedAt() == null
        ) {
            return false;
        }

        ResponseDTO<EntryResponseDto> entryResponseDto = entryClient.getEntry(applicationId);

        return entryResponseDto != null && entryResponseDto.getData() != null;

    }

    @Transactional(readOnly = true)
    @Override
    public List<RepaymentListResponseDto> get(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);
        return repayments.stream().map(RepaymentMapper::mapToRepaymentListResponseDto).collect(Collectors.toList());
    }

    @Override
    public RepaymentUpdateResponseDto update(Long repaymentId, RepaymentRequestDto repaymentRequestDto) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, "Repayment does not exist", HttpStatus.NOT_FOUND)
        );
        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();
        balanceClient.repaymentUpdate(
                applicationId,
                BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(BalanceRepaymentRequestDto.RepaymentType.ADD)
                        .build()
        );
        repayment.setRepaymentAmount(repaymentRequestDto.getRepaymentAmount());

        ResponseDTO<BalanceResponseDto> balanceResponseDto = balanceClient.repaymentUpdate(
                applicationId,
                BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(repaymentRequestDto.getRepaymentAmount())
                        .type(BalanceRepaymentRequestDto.RepaymentType.REMOVE)
                        .build()
        );

        return RepaymentUpdateResponseDto.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(repaymentRequestDto.getRepaymentAmount())
                .balance(balanceResponseDto.getData().getBalance())
                .createdAt(repayment.getCreatedAt())
                .createdBy(repayment.getCreatedBy())
                .updatedAt(repayment.getUpdatedAt())
                .updatedBy(repayment.getUpdatedBy())
                .build();
    }

    @Override
    public void delete(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, "Repayment does not exist", HttpStatus.NOT_FOUND)
        );

        Long applicationid = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

        balanceClient.repaymentUpdate(
                applicationid,
                BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(BalanceRepaymentRequestDto.RepaymentType.ADD)
                        .build()
        );

        repayment.setIsDeleted(true);
    }
}
