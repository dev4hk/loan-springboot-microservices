package com.example.judgement_server;

import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.entity.Judgement;
import com.example.judgement_server.repository.JudgementRepository;
import com.example.judgement_server.stubs.ApplicationStub;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
public class JudgementServerApplicationTests {

    @LocalServerPort
    private Integer port;

    @MockitoBean
    private JudgementRepository judgementRepository;

    private JudgementRequestDto judgementRequestDto;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        this.judgementRequestDto = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .approvalAmount(BigDecimal.TEN)
                .build();

        Judgement judgement = Judgement.builder()
                .judgementId(1L)
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .approvalAmount(BigDecimal.TEN)
                .build();

        when(judgementRepository.findByApplicationId(anyLong())).thenReturn(Optional.of(judgement));
        when(judgementRepository.findById(anyLong())).thenReturn(Optional.of(judgement));
        when(judgementRepository.save(Mockito.any(Judgement.class))).thenReturn(judgement);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void should_create_judgement() {
        ApplicationStub.stubApplicationGetCall(1L);
        String responseBody = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath jsonPath = new JsonPath(responseBody);
        String firstname = jsonPath.getString("data.firstname");
        String lastname = jsonPath.getString("data.lastname");

        assertNotNull(firstname);
        assertEquals("firstname", firstname);
        assertNotNull(lastname);
        assertEquals("lastname", lastname);
    }

    @Test
    void should_get_judgement() {
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath createJsonPath = new JsonPath(createResponse);
        Long judgmentId = createJsonPath.getLong("data.judgementId");

        String getResponse = RestAssured.given()
                .when()
                .get("/api/" + judgmentId)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath getJsonPath = new JsonPath(getResponse);
        String firstname = getJsonPath.getString("data.firstname");
        String lastname = getJsonPath.getString("data.lastname");
        BigDecimal approvalAmount = new BigDecimal(getJsonPath.getFloat("data.approvalAmount"));

        assertNotNull(firstname);
        assertEquals("firstname", firstname);
        assertNotNull(lastname);
        assertEquals("lastname", lastname);
        assertNotNull(approvalAmount);
        assertEquals(BigDecimal.TEN, approvalAmount);
    }

    @Test
    void should_get_judgment_of_application() {
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        String getResponse = RestAssured.given()
                .when()
                .get("/api/applications/1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath getJsonPath = new JsonPath(getResponse);
        String firstname = getJsonPath.getString("data.firstname");
        String lastname = getJsonPath.getString("data.lastname");
        BigDecimal approvalAmount = new BigDecimal(getJsonPath.getFloat("data.approvalAmount"));

        assertNotNull(firstname);
        assertEquals("firstname", firstname);
        assertNotNull(lastname);
        assertEquals("lastname", lastname);
        assertNotNull(approvalAmount);
        assertEquals(BigDecimal.TEN, approvalAmount);
    }

    @Test
    void should_update_judgement() {
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath createJsonPath = new JsonPath(createResponse);
        Long judgmentId = createJsonPath.getLong("data.judgementId");

        JudgementRequestDto updatedRequestDto = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("UpdatedFirstname")
                .lastname("UpdatedLastname")
                .approvalAmount(BigDecimal.valueOf(20))
                .build();

        String updateResponse = RestAssured.given()
                .contentType("application/json")
                .body(updatedRequestDto)
                .when()
                .put("/api/" + judgmentId)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath updateJsonPath = new JsonPath(updateResponse);
        String firstname = updateJsonPath.getString("data.firstname");
        String lastname = updateJsonPath.getString("data.lastname");
        BigDecimal approvalAmount = new BigDecimal(updateJsonPath.getFloat("data.approvalAmount"));

        assertNotNull(firstname);
        assertEquals("UpdatedFirstname", firstname);
        assertNotNull(lastname);
        assertEquals("UpdatedLastname", lastname);
        assertNotNull(approvalAmount);
        assertEquals(BigDecimal.valueOf(20), approvalAmount);
    }


    @Test
    void should_delete_judgement() {
        ApplicationStub.stubApplicationGetCall(1L);
        String responseBody = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath jsonPath = new JsonPath(responseBody);
        Long judgementId = jsonPath.getLong("data.judgementId");

        RestAssured.given()
                .when()
                .delete("/api/" + judgementId)
                .then()
                .log().all()
                .statusCode(200);

    }

    @Test
    void should_grant_judgement() {
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath createJsonPath = new JsonPath(createResponse);
        Long judgmentId = createJsonPath.getLong("data.judgementId");

        GrantAmountDto grantAmountDto = GrantAmountDto.builder()
                .applicationId(1L)
                .approvalAmount(BigDecimal.TEN)
                .build();
        ApplicationStub.stubUpdateGrantCall(1L, grantAmountDto);

        String grantResponse = RestAssured.given()
                .when()
                .patch("/api/" + judgmentId + "/grants")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath grantJsonPath = new JsonPath(grantResponse);
        Long applicationId = grantJsonPath.getLong("data.applicationId");
        BigDecimal approvalAmount = new BigDecimal(grantJsonPath.getFloat("data.approvalAmount"));

        assertNotNull(applicationId);
        assertEquals(1L, applicationId);
        assertNotNull(approvalAmount);
        assertEquals(BigDecimal.TEN, approvalAmount);
    }

    @Test
    void should_fail_when_applicationId_is_missing() {
        JudgementRequestDto judgementRequestDto = JudgementRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .approvalAmount(BigDecimal.TEN)
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(400)
                .body("data.applicationId", equalTo("ApplicationId is required"));
    }

    @Test
    void should_fail_when_firstname_is_invalid() {
        JudgementRequestDto judgementRequestDto = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("John123")
                .lastname("Doe")
                .approvalAmount(BigDecimal.TEN)
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(400)
                .body("data.firstname", equalTo("First name must contain only letters"));
    }

    @Test
    void should_fail_when_lastname_is_invalid() {
        JudgementRequestDto judgementRequestDto = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("John")
                .lastname("Doe123")
                .approvalAmount(BigDecimal.TEN)
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(400)
                .body("data.lastname", equalTo("Last name must contain only letters"));
    }

    @Test
    void should_fail_when_approval_amount_is_invalid() {
        JudgementRequestDto judgementRequestDto = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("John")
                .lastname("Doe")
                .approvalAmount(new BigDecimal("1234567890123456.00"))  // Exceeds the allowed precision
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(judgementRequestDto)
                .when()
                .post("/api")
                .then()
                .statusCode(400)
                .body("data.approvalAmount", equalTo("Hope amount must be a number with up to 2 decimal places"));
    }

}