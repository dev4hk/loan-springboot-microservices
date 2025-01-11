package com.example.counselserver.functions;

import com.example.counselserver.constants.CommunicationStatus;
import com.example.counselserver.dto.CounselMsgDto;
import com.example.counselserver.service.ICounselService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class CounselFunctions {

    private static final Logger logger = LoggerFactory.getLogger(CounselFunctions.class);

    @Bean
    public Consumer<CounselMsgDto> updateCommunication(ICounselService counselService) {
        return counselMsgDto -> {
            Long counselId = counselMsgDto.counselId();
            CommunicationStatus communicationStatus = counselMsgDto.communicationStatus();
            logger.debug("Updating Communication status for the counsel ID: " + counselId);
            counselService.updateCommunicationStatus(counselId, communicationStatus);
        };
    }
}
