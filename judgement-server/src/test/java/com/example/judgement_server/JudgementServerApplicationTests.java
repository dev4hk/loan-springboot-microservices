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
        // No need for clean-up since we are mocking the repository
    }

    @Test
    void should_create_judgement() {
        ApplicationStub.stubApplicationGetCall(1L);
        String responseBody = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/judgements")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // Use JsonPath to extract values
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
        // Create a judgement first
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/judgements")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath createJsonPath = new JsonPath(createResponse);
        Long judgmentId = createJsonPath.getLong("data.judgementId");

        // Now get the created judgement
        String getResponse = RestAssured.given()
                .when()
                .get("/judgements/" + judgmentId)
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
        // Create a judgement first
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/judgements")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // Now get the judgement by application ID
        String getResponse = RestAssured.given()
                .when()
                .get("/judgements/applications/1")
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
        // Create a judgement first
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/judgements")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath createJsonPath = new JsonPath(createResponse);
        Long judgmentId = createJsonPath.getLong("data.judgementId");

        // Update the created judgement
        JudgementRequestDto updatedRequestDto = JudgementRequestDto.builder()
                .applicationId(1L)
                .firstname("updated_firstname")
                .lastname("updated_lastname")
                .approvalAmount(BigDecimal.valueOf(20))
                .build();

        String updateResponse = RestAssured.given()
                .contentType("application/json")
                .body(updatedRequestDto)
                .when()
                .put("/judgements/" + judgmentId)
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
        assertEquals("updated_firstname", firstname);
        assertNotNull(lastname);
        assertEquals("updated_lastname", lastname);
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
                .post("/judgements")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath jsonPath = new JsonPath(responseBody);
        Long judgementId = jsonPath.getLong("data.judgementId");

        // Perform the soft delete operation
        RestAssured.given()
                .when()
                .delete("/judgements/" + judgementId)
                .then()
                .log().all()
                .statusCode(200);

    }

    @Test
    void should_grant_judgement() {
        // Create a judgement first
        ApplicationStub.stubApplicationGetCall(1L);
        String createResponse = RestAssured.given()
                .contentType("application/json")
                .body(this.judgementRequestDto)
                .when()
                .post("/judgements")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JsonPath createJsonPath = new JsonPath(createResponse);
        Long judgmentId = createJsonPath.getLong("data.judgementId");

        // Grant the created judgement
        GrantAmountDto grantAmountDto = GrantAmountDto.builder()
                .applicationId(1L)
                .approvalAmount(BigDecimal.TEN)
                .build();
        ApplicationStub.stubUpdateGrantCall(1L, grantAmountDto);

        String grantResponse = RestAssured.given()
                .when()
                .patch("/judgements/" + judgmentId + "/grants")
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

}