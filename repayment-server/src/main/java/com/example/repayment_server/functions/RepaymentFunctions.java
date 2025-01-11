package com.example.repayment_server.functions;

import com.example.repayment_server.constants.CommunicationStatus;
import com.example.repayment_server.dto.RepaymentMsgDto;
import com.example.repayment_server.service.IRepaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class RepaymentFunctions {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentFunctions.class);

    @Bean
    public Consumer<RepaymentMsgDto> updateCommunication(IRepaymentService repaymentService) {
        return repaymentMsgDto -> {
            Long repaymentId = repaymentMsgDto.repaymentId();
            CommunicationStatus communicationStatus = repaymentMsgDto.communicationStatus();
            logger.debug("Updating Communication status for the repayment ID: " + repaymentId);
            repaymentService.updateCommunicationStatus(repaymentId, communicationStatus);
        };
    }
}
