package com.example.repayment_server.service.impl;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.controller.RepaymentController;
import com.example.repayment_server.dto.*;
import com.example.repayment_server.entity.Repayment;
import com.example.repayment_server.exception.BaseException;
import com.example.repayment_server.mapper.RepaymentMapper;
import com.example.repayment_server.repository.RepaymentRepository;
import com.example.repayment_server.service.IRepaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class RepaymentServiceImpl implements IRepaymentService {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentServiceImpl.class);

    private final RepaymentRepository repaymentRepository;
    private final ApplicationClient applicationClient;
    private final BalanceClient balanceClient;
    private final EntryClient entryClient;

    @Override
    public RepaymentResponseDto create(Long applicationId, RepaymentRequestDto repaymentRequestDto) {
        logger.info("RepaymentServiceImpl - create invoked");
        logger.debug("RepaymentServiceImpl - applicationId: {}", applicationId);
        logger.debug("RepaymentServiceImpl - repaymentRequestDto: {}", repaymentRequestDto);
        if (!isRepayableApplication(applicationId)) {
            logger.error("RepaymentServiceImpl - Application not repayable");
            throw new BaseException(ResultType.BAD_REQUEST, "Application not repayable", HttpStatus.BAD_REQUEST);
        }

        Repayment repayment = RepaymentMapper.mapToRepayment(repaymentRequestDto);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);
        BalanceRepaymentRequestDto balanceRepaymentRequestDto = BalanceRepaymentRequestDto.builder()
                .repaymentAmount(repaymentRequestDto.getRepaymentAmount())
                .type(BalanceRepaymentRequestDto.RepaymentType.REMOVE)
                .build();
        List<BalanceRepaymentRequestDto> requestList = new ArrayList<>();
        requestList.add(balanceRepaymentRequestDto);
        ResponseDTO<List<BalanceResponseDto>> balanceResponseDto = balanceClient.repaymentUpdate(
                applicationId,
                requestList
        );
        RepaymentResponseDto repaymentResponseDto = RepaymentMapper.mapToRepaymentResponseDto(repayment);
        repaymentResponseDto.setBalance(balanceResponseDto.getData().getFirst().getBalance());

        return repaymentResponseDto;
    }

    private boolean isRepayableApplication(Long applicationId) {
        logger.info("RepaymentServiceImpl - isRepayableApplication invoked");
        logger.debug("RepaymentServiceImpl - applicationId: {}", applicationId);
        ResponseDTO<ApplicationResponseDto> applicationResponseDto = applicationClient.get(applicationId);
        if (applicationResponseDto.getData() == null
                || applicationResponseDto.getData().getContractedAt() == null
        ) {
            return false;
        }
        ResponseDTO<EntryResponseDto> entryResponseDto = entryClient.getEntry(applicationId);
        return entryResponseDto.getData() != null;

    }

    @Transactional(readOnly = true)
    @Override
    public List<RepaymentListResponseDto> get(Long applicationId) {
        logger.info("RepaymentServiceImpl - get invoked");
        logger.debug("RepaymentServiceImpl - applicationId: {}", applicationId);
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);
        return repayments.stream().map(RepaymentMapper::mapToRepaymentListResponseDto).collect(Collectors.toList());
    }

    @Override
    public RepaymentUpdateResponseDto update(Long repaymentId, RepaymentRequestDto repaymentRequestDto) {
        logger.info("RepaymentServiceImpl - update invoked");
        logger.debug("RepaymentServiceImpl - repaymentId: {}", repaymentId);
        logger.debug("RepaymentServiceImpl - repaymentRequestDto: {}", repaymentRequestDto);
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                {
                    logger.error("RepaymentServiceImpl - Repayment does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Repayment does not exist", HttpStatus.NOT_FOUND);
                }
        );
        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();
        repayment.setRepaymentAmount(repaymentRequestDto.getRepaymentAmount());

        List<BalanceRepaymentRequestDto> repaymentRequestList = new ArrayList<>();
        Collections.addAll(repaymentRequestList,
                BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(BalanceRepaymentRequestDto.RepaymentType.ADD)
                        .build(),
                BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(BalanceRepaymentRequestDto.RepaymentType.ADD)
                        .build()
        );
        ResponseDTO<List<BalanceResponseDto>> response = balanceClient.repaymentUpdate(
                applicationId,
                repaymentRequestList
        );

        return RepaymentUpdateResponseDto.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(repaymentRequestDto.getRepaymentAmount())
                .balance(response.getData().getLast().getBalance())
                .createdAt(repayment.getCreatedAt())
                .createdBy(repayment.getCreatedBy())
                .updatedAt(repayment.getUpdatedAt())
                .updatedBy(repayment.getUpdatedBy())
                .build();
    }

    @Override
    public void delete(Long repaymentId) {
        logger.info("RepaymentServiceImpl - delete invoked");
        logger.debug("RepaymentServiceImpl - repaymentId: {}", repaymentId);
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, "Repayment does not exist", HttpStatus.NOT_FOUND)
        );

        Long applicationid = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();
        balanceClient.repaymentUpdate(
                applicationid,
                List.of(BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(BalanceRepaymentRequestDto.RepaymentType.ADD)
                        .build())
        );

        repayment.setIsDeleted(true);
    }
}
