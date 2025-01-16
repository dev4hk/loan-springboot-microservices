package com.example.entry_server;

import com.example.entry_server.client.ApplicationClient;
import com.example.entry_server.client.BalanceClient;
import com.example.entry_server.client.dto.ApplicationResponseDto;
import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceResponseDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.example.entry_server.constants.ResultType;
import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.dto.ResponseDTO;
import com.example.entry_server.dto.ResultObject;
import com.example.entry_server.entity.Entry;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EntryServerApplicationTests {

    @LocalServerPort
    private Integer port;

    @MockitoBean
    private ApplicationClient applicationClient;

    @MockitoBean
    private BalanceClient balanceClient;

    @MockitoBean
    private StreamBridge streamBridge;

    private ApplicationResponseDto applicationResponseContractedDto;
    private BalanceResponseDto balanceResponseDto;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        applicationResponseContractedDto = ApplicationResponseDto.builder()
                .applicationId(1L)
                .hopeAmount(BigDecimal.valueOf(5000))
                .approvalAmount(BigDecimal.valueOf(5000))
                .appliedAt(LocalDateTime.now())
                .contractedAt(LocalDateTime.now())
                .build();

        balanceResponseDto = BalanceResponseDto.builder()
                .applicationId(1L)
                .balanceId(1L)
                .balance(BigDecimal.valueOf(4000))
                .build();

        Entry entry = Entry.builder()
                .entryId(1L)
                .applicationId(1L)
                .entryAmount(BigDecimal.valueOf(1000))
                .build();

        when(applicationClient.get(anyLong())).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), applicationResponseContractedDto));
        when(balanceClient.create(anyLong(), any(BalanceRequestDto.class))).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), balanceResponseDto));
        when(balanceClient.update(anyLong(), any(BalanceUpdateRequestDto.class))).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), balanceResponseDto));
    }

    @Order(1)
    @Test
    void should_create_entry() {
        Long applicationId = 1L;

        EntryRequestDto request = EntryRequestDto.builder()
                .entryAmount(BigDecimal.valueOf(1000.00))
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post("/api/" + applicationId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.entryId", equalTo(1))
                .body("data.entryAmount", equalTo(1000));

    }

    @Order(2)
    @Test
    void should_get_entry() {
        Long applicationId = 1L;

        RestAssured.given()
                .contentType("application/json")
                .get("/api/" + applicationId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.applicationId", equalTo(1))
                .body("data.entryAmount", equalTo(1000));
    }

    @Order(3)
    @Test
    void should_update_entry() {
        Long entryId = 1L;
        Long applicationId = 1L;

        EntryRequestDto request = EntryRequestDto.builder()
                .entryAmount(BigDecimal.valueOf(2000.00))
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .put("/api/" + entryId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.entryId", equalTo(1))
                .body("data.afterEntryAmount", equalTo(2000.00F));
    }

    @Order(4)
    @Test
    void should_delete_entry() {
        Long entryId = 1L;
        Long applicationId = 1L;

        RestAssured.given()
                .contentType("application/json")
                .delete("/api/" + entryId)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(5)
    @Test
    void should_fail_to_create_entry_with_missing_entryAmount() {
        Long applicationId = 1L;

        EntryRequestDto request = EntryRequestDto.builder().build();

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post("/api/" + applicationId)
                .then()
                .statusCode(400);
    }

    @Order(6)
    @Test
    void should_fail_to_update_entry_with_invalid_entryAmount() {
        Long entryId = 1L;
        Long applicationId = 1L;

        EntryRequestDto request = EntryRequestDto.builder()
                .entryAmount(BigDecimal.valueOf(-2000.003))
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .put("/api/" + entryId)
                .then()
                .statusCode(400);
    }

    @Order(7)
    @Test
    void should_fail_to_delete_nonexistent_entry() {
        Long entryId = 999L;
        Long applicationId = 1L;

        RestAssured.given()
                .contentType("application/json")
                .delete("/api/" + entryId)
                .then()
                .statusCode(404);
    }

}
