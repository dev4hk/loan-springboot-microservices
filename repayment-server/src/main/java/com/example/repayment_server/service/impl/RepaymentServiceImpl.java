package com.example.repayment_server.service.impl;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.constants.CommunicationStatus;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.*;
import com.example.repayment_server.entity.Repayment;
import com.example.repayment_server.exception.BaseException;
import com.example.repayment_server.mapper.RepaymentMapper;
import com.example.repayment_server.repository.RepaymentRepository;
import com.example.repayment_server.service.IRepaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
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
    private final StreamBridge streamBridge;

    @Override
    public RepaymentResponseDto create(Long applicationId, RepaymentRequestDto repaymentRequestDto) {
        logger.info("RepaymentServiceImpl - create invoked");

        ResponseDTO<BalanceResponseDto> balanceResponse = balanceClient.get(applicationId);
        if(balanceResponse != null && balanceResponse.getData().getBalance().equals(BigDecimal.ZERO)) {
            throw new BaseException(ResultType.BAD_REQUEST, "No Balance found", HttpStatus.BAD_REQUEST);
        }

        ApplicationResponseDto applicationResponseDto = checkRepayableAndGetApplication(applicationId);

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
        sendCommunication(repayment, applicationResponseDto, CommunicationStatus.REPAYMENT_CREATED);
        if(repaymentResponseDto.getBalance().equals(BigDecimal.ZERO)) {
            sendCommunication(repayment, applicationResponseDto, CommunicationStatus.REPAYMENT_COMPLETE);
            applicationClient.complete(applicationId);
        }
        return repaymentResponseDto;
    }

    private ApplicationResponseDto checkRepayableAndGetApplication(Long applicationId) {
        logger.info("RepaymentServiceImpl - isRepayableApplication invoked");
        ResponseDTO<ApplicationResponseDto> applicationResponseDto = applicationClient.get(applicationId);
        if (applicationResponseDto.getData().getContractedAt() == null) {
            logger.error("RepaymentServiceImpl - Application not contracted");
            throw new BaseException(ResultType.BAD_REQUEST, "Application not contracted", HttpStatus.BAD_REQUEST);
        }
        ResponseDTO<EntryResponseDto> entryResponseDto = entryClient.getEntry(applicationId);
        return applicationResponseDto.getData();
    }

    @Transactional(readOnly = true)
    @Override
    public List<RepaymentListResponseDto> get(Long applicationId) {
        logger.info("RepaymentServiceImpl - get invoked");
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);
        return repayments.stream().map(RepaymentMapper::mapToRepaymentListResponseDto).collect(Collectors.toList());
    }

    @Override
    public RepaymentUpdateResponseDto update(Long repaymentId, RepaymentRequestDto repaymentRequestDto) {
        logger.info("RepaymentServiceImpl - update invoked");
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
                        .repaymentAmount(repaymentRequestDto.getRepaymentAmount())
                        .type(BalanceRepaymentRequestDto.RepaymentType.REMOVE)
                        .build()
        );
        ResponseDTO<List<BalanceResponseDto>> response = balanceClient.repaymentUpdate(
                applicationId,
                repaymentRequestList
        );

        ApplicationResponseDto applicationResponseDto = applicationClient.get(applicationId).getData();

        sendCommunication(repayment, applicationResponseDto, CommunicationStatus.REPAYMENT_UPDATED);

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
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                new BaseException(ResultType.RESOURCE_NOT_FOUND, "Repayment does not exist", HttpStatus.NOT_FOUND)
        );

        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();
        balanceClient.repaymentUpdate(
                applicationId,
                List.of(BalanceRepaymentRequestDto.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(BalanceRepaymentRequestDto.RepaymentType.ADD)
                        .build())
        );

        ApplicationResponseDto applicationResponseDto = applicationClient.get(applicationId).getData();
        sendCommunication(repayment, applicationResponseDto, CommunicationStatus.REPAYMENT_UPDATED);
        repayment.setIsDeleted(true);
    }

    @Override
    public void updateCommunicationStatus(Long repaymentId, CommunicationStatus communicationStatus) {
        logger.info("RepaymentServiceImpl - updateCommunicationStatus invoked");
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                {
                    logger.error("RepaymentServiceImpl - Repayment does not exist");
                    return new BaseException(ResultType.RESOURCE_NOT_FOUND, "Repayment does not exist", HttpStatus.NOT_FOUND);
                }
        );
        repayment.setCommunicationStatus(communicationStatus);
    }

    private void sendCommunication(Repayment repayment, ApplicationResponseDto applicationResponseDto, CommunicationStatus communicationStatus) {
        var repaymentMsgDto = new RepaymentMsgDto(
                repayment.getRepaymentId(),
                repayment.getApplicationId(),
                repayment.getRepaymentAmount(),
                applicationResponseDto.getFirstname(),
                applicationResponseDto.getLastname(),
                applicationResponseDto.getCellPhone(),
                applicationResponseDto.getEmail(),
                communicationStatus
        );
        logger.debug("Sending Communication request for the details: {}", repaymentMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", repaymentMsgDto);
        logger.debug("Is the Communication request successfully invoked?: {}", result);
    }
}
