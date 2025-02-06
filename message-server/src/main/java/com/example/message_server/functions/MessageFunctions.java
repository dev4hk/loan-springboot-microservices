package com.example.message_server.functions;

import com.example.message_server.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);


    @Bean
    public Function<ApplicationMsgDto, ApplicationMsgDto> applicationEmail() {
        return applicationMsgDto -> {
            logger.info("Sending application email with the details: " + applicationMsgDto.toString());
            return applicationMsgDto;
        };
    }

    @Bean
    public Function<ApplicationMsgDto, ApplicationMsgDto> applicationSms() {
        return applicationMsgDto -> {
            logger.info("Sending application sms with the details: " + applicationMsgDto.toString());
            return applicationMsgDto;
        };
    }

    @Bean
    public Function<EntryMsgDto, EntryMsgDto> entryEmail() {
        return entryMsgDto -> {
            logger.info("Sending entry email with the details: " + entryMsgDto.toString());
            return entryMsgDto;
        };
    }

    @Bean
    public Function<EntryMsgDto, EntryMsgDto> entrySms() {
        return entryMsgDto -> {
            logger.info("Sending entry sms with the details: " + entryMsgDto.toString());
            return entryMsgDto;
        };
    }

    @Bean
    public Function<RepaymentMsgDto, RepaymentMsgDto> repaymentEmail() {
        return repaymentMsgDto -> {
            logger.info("Sending repayment email with the details: " + repaymentMsgDto.toString());
            return repaymentMsgDto;
        };
    }

    @Bean
    public Function<RepaymentMsgDto, RepaymentMsgDto> repaymentSms() {
        return repaymentMsgDto -> {
            logger.info("Sending repayment sms with the details: " + repaymentMsgDto.toString());
            return repaymentMsgDto;
        };
    }

    @Bean
    public Function<CounselMsgDto, CounselMsgDto> counselEmail() {
        return counselMsgDto -> {
            logger.info("Sending counsel email with the details: " + counselMsgDto.toString());
            return counselMsgDto;
        };
    }

    @Bean
    public Function<CounselMsgDto, CounselMsgDto> counselSms() {
        return counselMsgDto -> {
            logger.info("Sending counsel sms with the details: " + counselMsgDto.toString());
            return counselMsgDto;
        };
    }

    @Bean
    public Function<JudgementMsgDto, JudgementMsgDto> judgementEmail() {
        return judgementMsgDto -> {
            logger.info("Sending judgement email with the details: " + judgementMsgDto.toString());
            return judgementMsgDto;
        };
    }

    @Bean
    public Function<JudgementMsgDto, JudgementMsgDto> judgementSms() {
        return judgementMsgDto -> {
            logger.info("Sending judgement sms with the details: " + judgementMsgDto.toString());
            return judgementMsgDto;
        };
    }

}
