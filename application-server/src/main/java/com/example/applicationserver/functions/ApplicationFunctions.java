package com.example.applicationserver.functions;

import com.example.applicationserver.constants.CommunicationStatus;
import com.example.applicationserver.dto.ApplicationMsgDto;
import com.example.applicationserver.service.IApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
public class ApplicationFunctions {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationFunctions.class);

    @Bean
    public Consumer<ApplicationMsgDto> updateCommunication(IApplicationService applicationService) {
        return applicationMsgDto -> {
            Long applicationId = applicationMsgDto.applicationId();
            CommunicationStatus communicationStatus = applicationMsgDto.communicationStatus();
            logger.debug("Updating Communication status for the application ID: " + applicationId);
            applicationService.updateCommunicationStatus(applicationId, communicationStatus);
        };
    }

}
