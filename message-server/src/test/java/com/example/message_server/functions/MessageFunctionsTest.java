package com.example.message_server.functions;

import com.example.message_server.constant.CommunicationStatus;
import com.example.message_server.dto.ApplicationMsgDto;
import com.example.message_server.dto.CounselMsgDto;
import com.example.message_server.dto.EntryMsgDto;
import com.example.message_server.dto.RepaymentMsgDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class MessageFunctionsTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testApplicationEmailFunction() {
        Function<ApplicationMsgDto, ApplicationMsgDto> applicationEmail = (Function<ApplicationMsgDto, ApplicationMsgDto>) applicationContext.getBean("applicationEmail");
        ApplicationMsgDto applicationMsgDto = new ApplicationMsgDto(1L, "John", "Doe", "john.doe@example.com", "1234567890", CommunicationStatus.APPLICATION_RECEIVED);
        ApplicationMsgDto result = applicationEmail.apply(applicationMsgDto);

        assertEquals(applicationMsgDto, result);
    }

    @Test
    void testApplicationSmsFunction() {
        Function<ApplicationMsgDto, ApplicationMsgDto> applicationSms = (Function<ApplicationMsgDto, ApplicationMsgDto>) applicationContext.getBean("applicationSms");
        ApplicationMsgDto applicationMsgDto = new ApplicationMsgDto(1L, "John", "Doe", "john.doe@example.com", "1234567890", CommunicationStatus.APPLICATION_RECEIVED);
        ApplicationMsgDto result = applicationSms.apply(applicationMsgDto);

        assertEquals(applicationMsgDto, result);
    }

    @Test
    void testEntryEmailFunction() {
        Function<EntryMsgDto, EntryMsgDto> entryEmail = (Function<EntryMsgDto, EntryMsgDto>) applicationContext.getBean("entryEmail");
        EntryMsgDto entryMsgDto = new EntryMsgDto(1L, 2L, BigDecimal.valueOf(100.00), "John", "Doe", "1234567890", "john.doe@example.com", CommunicationStatus.ENTRY_CREATED);
        EntryMsgDto result = entryEmail.apply(entryMsgDto);

        assertEquals(entryMsgDto, result);
    }

    @Test
    void testEntrySmsFunction() {
        Function<EntryMsgDto, EntryMsgDto> entrySms = (Function<EntryMsgDto, EntryMsgDto>) applicationContext.getBean("entrySms");
        EntryMsgDto entryMsgDto = new EntryMsgDto(1L, 2L, BigDecimal.valueOf(100.00), "John", "Doe", "1234567890", "john.doe@example.com", CommunicationStatus.ENTRY_CREATED);
        EntryMsgDto result = entrySms.apply(entryMsgDto);

        assertEquals(entryMsgDto, result);
    }

    @Test
    void testRepaymentEmailFunction() {
        Function<RepaymentMsgDto, RepaymentMsgDto> repaymentEmail = (Function<RepaymentMsgDto, RepaymentMsgDto>) applicationContext.getBean("repaymentEmail");
        RepaymentMsgDto repaymentMsgDto = new RepaymentMsgDto(1L, 2L, BigDecimal.valueOf(50.00), "John", "Doe", "1234567890", "john.doe@example.com", CommunicationStatus.REPAYMENT_CREATED);
        RepaymentMsgDto result = repaymentEmail.apply(repaymentMsgDto);

        assertEquals(repaymentMsgDto, result);
    }

    @Test
    void testRepaymentSmsFunction() {
        Function<RepaymentMsgDto, RepaymentMsgDto> repaymentSms = (Function<RepaymentMsgDto, RepaymentMsgDto>) applicationContext.getBean("repaymentSms");
        RepaymentMsgDto repaymentMsgDto = new RepaymentMsgDto(1L, 2L, BigDecimal.valueOf(50.00), "John", "Doe", "1234567890", "john.doe@example.com", CommunicationStatus.REPAYMENT_CREATED);
        RepaymentMsgDto result = repaymentSms.apply(repaymentMsgDto);

        assertEquals(repaymentMsgDto, result);
    }

    @Test
    void testCounselEmailFunction() {
        Function<CounselMsgDto, CounselMsgDto> counselEmail = (Function<CounselMsgDto, CounselMsgDto>) applicationContext.getBean("counselEmail");
        CounselMsgDto counselMsgDto = new CounselMsgDto(1L, "John", "Doe", "1234567890", "john.doe@example.com", CommunicationStatus.COUNSEL_CREATED);
        CounselMsgDto result = counselEmail.apply(counselMsgDto);

        assertEquals(counselMsgDto, result);
    }

    @Test
    void testCounselSmsFunction() {
        Function<CounselMsgDto, CounselMsgDto> counselSms = (Function<CounselMsgDto, CounselMsgDto>) applicationContext.getBean("counselSms");
        CounselMsgDto counselMsgDto = new CounselMsgDto(1L, "John", "Doe", "1234567890", "john.doe@example.com", CommunicationStatus.COUNSEL_CREATED);
        CounselMsgDto result = counselSms.apply(counselMsgDto);

        assertEquals(counselMsgDto, result);
    }
}
