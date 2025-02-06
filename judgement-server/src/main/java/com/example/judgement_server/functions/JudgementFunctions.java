package com.example.judgement_server.functions;

import com.example.judgement_server.constants.CommunicationStatus;
import com.example.judgement_server.dto.JudgementMsgDto;
import com.example.judgement_server.service.IJudgementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class JudgementFunctions {

    private static final Logger logger = LoggerFactory.getLogger(JudgementFunctions.class);

    @Bean
    public Consumer<JudgementMsgDto> updateCommunication(IJudgementService judgementService) {
        return judgementMsgDto -> {
            Long judgementId = judgementMsgDto.JudgementId();
            CommunicationStatus communicationStatus = judgementMsgDto.communicationStatus();
            logger.debug("Updating Communication status for the judgement ID: " + judgementId);
            judgementService.updateCommunicationStatus(judgementId, communicationStatus);
        };
    }
}
