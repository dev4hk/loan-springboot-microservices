package com.example.applicationserver.functions;

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
    public Consumer<Long> updateCommunication(IApplicationService applicationService) {
        return applicationId -> {
            logger.debug("Updating Communication status for the application ID: " + applicationId);
            applicationService.updateCommunicationStatus(applicationId);
        };
    }

}
