package com.example.entry_server.functions;

import com.example.entry_server.constants.CommunicationStatus;
import com.example.entry_server.dto.EntryMsgDto;
import com.example.entry_server.service.IEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EntryFunctions {

    private static final Logger logger = LoggerFactory.getLogger(EntryFunctions.class);

    @Bean
    public Consumer<EntryMsgDto> updateCommunication(IEntryService entryService) {
        return entryMsgDto -> {
            Long entryId = entryMsgDto.entryId();
            CommunicationStatus communicationStatus = entryMsgDto.communicationStatus();
            logger.debug("Updating Communication status for the entry ID: " + entryMsgDto.entryId());
            entryService.updateCommunicationStatus(entryId, communicationStatus);
        };
    }

}
