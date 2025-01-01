package com.example.entry_server;

import com.example.entry_server.client.dto.BalanceRequestDto;
import com.example.entry_server.client.dto.BalanceUpdateRequestDto;
import com.example.entry_server.dto.EntryRequestDto;
import com.example.entry_server.stubs.ApplicationStub;
import com.example.entry_server.stubs.BalanceStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EntryServerApplicationTests {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Order(1)
    @Test
    void should_create_entry() {
        Long applicationId = 1L;

        EntryRequestDto request = EntryRequestDto.builder()
                .entryAmount(BigDecimal.valueOf(1000.00))
                .build();


        ApplicationStub.stubApplicationGetCallContracted(applicationId);
        BalanceStub.stubCreateBalance(
                applicationId,
                BalanceRequestDto.builder()
                        .applicationId(1L)
                        .entryAmount(BigDecimal.valueOf(1000.00))
                        .build()
        );

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post("/entries/" + applicationId)
                .then()
                .statusCode(200)
                .body("data.entryId", equalTo(1))
                .body("data.entryAmount", equalTo(1000.00F));

    }

    @Order(2)
    @Test
    void should_get_entry() {
        Long applicationId = 1L;

        ApplicationStub.stubApplicationGetCallContracted(applicationId);

        RestAssured.given()
                .contentType("application/json")
                .get("/entries/" + applicationId)
                .then()
                .statusCode(200)
                .body("data.applicationId", equalTo(1))
                .body("data.entryAmount", equalTo(1000.00F));
    }

    @Order(3)
    @Test
    void should_update_entry() {
        Long entryId = 1L;
        Long applicationId = 1L;

        EntryRequestDto request = EntryRequestDto.builder()
                .entryAmount(BigDecimal.valueOf(2000.00))
                .build();

        BalanceStub.stubUpdateBalance(
                applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(BigDecimal.valueOf(1000.00))
                        .afterEntryAmount(BigDecimal.valueOf(2000.00))
                        .build()
        );

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .put("/entries/" + entryId)
                .then()
                .statusCode(200)
                .body("data.entryId", equalTo(1))
                .body("data.afterEntryAmount", equalTo(2000.00F));
    }

    @Order(4)
    @Test
    void should_delete_entry() {
        Long entryId = 1L;
        Long applicationId = 1L;

        BalanceStub.stubUpdateBalance(
                applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(BigDecimal.valueOf(2000.00))
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build()
        );

        RestAssured.given()
                .contentType("application/json")
                .delete("/entries/" + entryId)
                .then()
                .statusCode(200);
    }
    @Order(5)
    @Test
    void should_fail_to_create_entry_with_missing_entryAmount() {
        Long applicationId = 1L;

        EntryRequestDto request = EntryRequestDto.builder().build();

        ApplicationStub.stubApplicationGetCallContracted(applicationId);

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .post("/entries/" + applicationId)
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

        BalanceStub.stubUpdateBalance(
                applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(BigDecimal.valueOf(1000.00))
                        .afterEntryAmount(BigDecimal.valueOf(-2000.003))
                        .build()
        );

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .put("/entries/" + entryId)
                .then()
                .statusCode(400);
    }

    @Order(7)
    @Test
    void should_fail_to_delete_nonexistent_entry() {
        Long entryId = 999L;
        Long applicationId = 1L;

        BalanceStub.stubUpdateBalance(
                applicationId,
                BalanceUpdateRequestDto.builder()
                        .applicationId(applicationId)
                        .beforeEntryAmount(BigDecimal.valueOf(2000.00))
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build()
        );

        RestAssured.given()
                .contentType("application/json")
                .delete("/entries/" + entryId)
                .then()
                .statusCode(404);
    }


}
