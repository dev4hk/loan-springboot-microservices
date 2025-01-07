package com.example.repayment_server;

import com.example.repayment_server.client.ApplicationClient;
import com.example.repayment_server.client.BalanceClient;
import com.example.repayment_server.client.EntryClient;
import com.example.repayment_server.client.dto.ApplicationResponseDto;
import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.client.dto.BalanceResponseDto;
import com.example.repayment_server.client.dto.EntryResponseDto;
import com.example.repayment_server.constants.ResultType;
import com.example.repayment_server.dto.RepaymentRequestDto;
import com.example.repayment_server.dto.ResponseDTO;
import com.example.repayment_server.dto.ResultObject;
import com.example.repayment_server.entity.Repayment;
import com.example.repayment_server.repository.RepaymentRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepaymentServerApplicationTests {

    @LocalServerPort
    private Integer port;

    @MockitoBean
    private EntryClient entryClient;

    @MockitoBean
    private ApplicationClient applicationclient;

    @MockitoBean
    private BalanceClient balanceClient;

    @MockitoBean
    private RepaymentRepository repaymentRepository;

    private ApplicationResponseDto applicationResponseContractedDto;
    private ApplicationResponseDto applicationResponseNonContractedDto;
    private BalanceResponseDto balanceResponseDto;
    private EntryResponseDto entryResponseDto;


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

        applicationResponseNonContractedDto = ApplicationResponseDto.builder()
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

        Repayment repayment = Repayment.builder()
                .repaymentId(1L)
                .applicationId(1L)
                .repaymentAmount(BigDecimal.valueOf(1000))
                .build();

        when(repaymentRepository.save(any(Repayment.class))).thenReturn(repayment);
        when(repaymentRepository.findAllByApplicationId(anyLong())).thenReturn(List.of(repayment));
        when(repaymentRepository.findById(anyLong())).thenReturn(Optional.of(repayment));
        when(balanceClient.repaymentUpdate(anyLong(), anyList())).thenReturn(
                new ResponseDTO<>(
                        new ResultObject(ResultType.SUCCESS, "success"),
                        new ArrayList<>(Collections.singletonList(balanceResponseDto)))
        );

        when(entryClient.getEntry(1L)).thenReturn(new ResponseDTO<>(
                new ResultObject(ResultType.SUCCESS, "success"),
                new EntryResponseDto(
                1L,
                1L,
                BigDecimal.valueOf(1000),
                LocalDateTime.now(),
                "ENTRY_MS",
                LocalDateTime.now(),
                "ENTRY_MS"
        )));
    }

    @Order(1)
    @Test
    void should_create_repayment() {
        Long applicationId = 1L;

        RepaymentRequestDto request = RepaymentRequestDto.builder()
                .repaymentAmount(BigDecimal.valueOf(1000.00))
                .build();

        when(applicationclient.get(anyLong())).thenReturn(new ResponseDTO<>(
                new ResultObject(ResultType.SUCCESS, "success"),
                applicationResponseContractedDto)
        );

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post("/api/" + applicationId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.repaymentAmount", equalTo(1000.00F));
    }

    @Order(2)
    @Test
    void should_get_repayments() {
        Long applicationId = 1L;

        RestAssured.given()
                .contentType("application/json")
                .get("/api/" + applicationId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data[0].repaymentId", equalTo(1));
    }

    @Order(3)
    @Test
    void should_update_repayment() {
        Long repaymentId = 1L;
        Long applicationId = 1L;

        RepaymentRequestDto request = RepaymentRequestDto.builder()
                .repaymentAmount(BigDecimal.valueOf(2000.00))
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .put("/api/" + repaymentId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.applicationId", equalTo(1))
                .body("data.afterRepaymentAmount", equalTo(2000.00F));
    }

    @Order(4)
    @Test
    void should_delete_repayment() {
        Long repaymentId = 1L;
        Long applicationId = 1L;

        RestAssured.given()
                .contentType("application/json")
                .delete("/api/" + repaymentId)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(5)
    @Test
    void should_fail_to_create_repayment_with_missing_repaymentAmount() {
        Long applicationId = 1L;

        RepaymentRequestDto request = RepaymentRequestDto.builder().build();

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post("/api/" + applicationId)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Order(6)
    @Test
    void should_fail_to_update_repayment_with_invalid_repaymentAmount() {
        Long repaymentId = 1L;
        Long applicationId = 1L;

        RepaymentRequestDto request = RepaymentRequestDto.builder()
                .repaymentAmount(BigDecimal.valueOf(2000.003))
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .put("/api/" + repaymentId)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Order(7)
    @Test
    void should_fail_to_delete_nonexistent_repayment() {
        Long repaymentId = 999L;

        when(repaymentRepository.findById(repaymentId)).thenReturn(Optional.empty());

        RestAssured.given()
                .contentType("application/json")
                .delete("/api/" + repaymentId)
                .then()
                .log().all()
                .statusCode(404);
    }

}
