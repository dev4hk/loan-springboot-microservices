package com.example.judgement_server;

import com.example.judgement_server.client.ApplicationClient;
import com.example.judgement_server.client.dto.ApplicationResponseDto;
import com.example.judgement_server.constants.ResultType;
import com.example.judgement_server.dto.GrantAmountDto;
import com.example.judgement_server.dto.JudgementRequestDto;
import com.example.judgement_server.dto.ResponseDTO;
import com.example.judgement_server.dto.ResultObject;
import com.example.judgement_server.entity.Judgement;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JudgementServerApplicationTests {

    @LocalServerPort
    private Integer port;

    @MockitoBean
    private ApplicationClient applicationClient;

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

        ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
        applicationResponseDto.setApplicationId(1L);
        ResponseDTO<ApplicationResponseDto> responseDTO = new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), applicationResponseDto);
        when(applicationClient.get(anyLong())).thenReturn(responseDTO);
        when(applicationClient.updateGrant(anyLong(), Mockito.any(GrantAmountDto.class))).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), null));
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void should_create_judgement() {
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

    @Order(2)
    @Test
    void should_get_judgement() {
        String getResponse = RestAssured.given()
                .when()
                .get("/api/" + 1L)
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

    @Order(3)
    @Test
    void should_get_judgment_of_application() {
        String getResponse = RestAssured.given()
                .when()
                .get("/api/applications/1")
                .then()
                .log().all()
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

    @Order(4)
    @Test
    void should_update_judgement() {
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
                .put("/api/" + 1L)
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

    @Order(5)
    @Test
    void should_grant_judgement() {
        GrantAmountDto grantAmountDto = GrantAmountDto.builder()
                .applicationId(1L)
                .approvalAmount(BigDecimal.TEN)
                .build();

        String grantResponse = RestAssured.given()
                .when()
                .patch("/api/" + 1L + "/grants")
                .then()
                .log().all()
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
        assertEquals(BigDecimal.valueOf(20), approvalAmount);
    }

    @Order(6)
    @Test
    void should_delete_judgement() {
        RestAssured.given()
                .when()
                .delete("/api/" + 1L)
                .then()
                .log().all()
                .statusCode(200);

    }

    @Test
    void should_fail_when_delete_non_existing_judgement() {
        RestAssured.given()
                .when()
                .delete("/api/" + 999L)
                .then()
                .log().all()
                .statusCode(404);

    }

}