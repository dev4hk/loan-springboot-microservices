package com.example.message_server.functions;

import com.example.message_server.dto.ApplicationMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);


    @Bean
    public Function<ApplicationMsgDto, ApplicationMsgDto> email() {
        return applicationMsgDto -> {
            logger.info("Sending email with the details: " + applicationMsgDto.toString());
            return applicationMsgDto;
        };
    }

    @Bean
    public Function<ApplicationMsgDto, ApplicationMsgDto> sms() {
        return applicationMsgDto -> {
            logger.info("Sending sms with the details: " + applicationMsgDto.toString());
            return applicationMsgDto;
        };
    }

}
