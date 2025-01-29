package com.example.applicationserver;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.CounselClient;
import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.*;
import com.example.applicationserver.constants.ResultType;
import com.example.applicationserver.dto.ApplicationMsgDto;
import com.example.applicationserver.dto.GrantAmountDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.dto.ResultObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationServerApplicationTests {

    @LocalServerPort
    private int port;

    @MockitoBean
    private TermsClient termsClient;

    @MockitoBean
    private AcceptTermsClient acceptTermsClient;

    @MockitoBean
    private JudgementClient judgementClient;

    @MockitoBean
    private CounselClient counselClient;

    @MockitoBean
    private StreamBridge streamBridge;

    private TermsResponseDto termsResponseDto;

    private AcceptTermsResponseDto acceptTermsResponseDto;

    private CounselResponseDto counselResponseDto;

    private JudgementResponseDto judgementResponseDto;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        termsResponseDto = TermsResponseDto.builder()
                .name("term1")
                .termsId(1L)
                .termsDetailUrl("http://someurl")
                .build();

        acceptTermsResponseDto = AcceptTermsResponseDto.builder()
                .applicationId(1L)
                .acceptTermsId(1L)
                .termsId(1L)
                .build();

        judgementResponseDto = JudgementResponseDto.builder()
                .applicationId(1L)
                .judgementId(1L)
                .approvalAmount(BigDecimal.valueOf(5000))
                .build();

        counselResponseDto = CounselResponseDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .cellPhone("0123456789")
                .email("email@email.com")
                .address1("address")
                .appliedAt(LocalDateTime.now())
                .counselId(1L)
                .memo("memo")
                .build();

        when(termsClient.getAll()).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), List.of(termsResponseDto)));
        when(acceptTermsClient.create(any(AcceptTermsRequestDto.class))).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), List.of(acceptTermsResponseDto)));
        when(counselClient.getByEmail(anyString())).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), counselResponseDto));
        when(judgementClient.getJudgmentOfApplication(anyLong())).thenReturn(new ResponseDTO<>(new ResultObject(ResultType.SUCCESS, "success"), judgementResponseDto));
        when(streamBridge.send(anyString(), any(ApplicationMsgDto.class))).thenReturn(true);
    }

    @Order(1)
    @Test
    void should_create_application() {
        String requestDto = """
                {
                        "firstname": "firstname",
                        "lastname": "lastname",
                        "cellPhone": "1111111111",
                        "email": "email@email.com",
                        "hopeAmount": 1000.00
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/api")
                .then()
                .statusCode(200)
                .body("data.firstname", equalTo("firstname"));
    }

    @Order(2)
    @Test
    void should_get_application() {
        RestAssured.given()
                .get("/api/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.firstname", equalTo("firstname"));
    }

    @Order(2)
    @Test
    void should_get_application_by_email() {
        String email = "email@email.com";
        RestAssured.given()
                .queryParam("email", email)
                .get("/api/email")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.firstname", equalTo("firstname"));
    }

    @Order(4)
    @Test
    void should_throw_exception_when_request_non_exist_application_id() {
        RestAssured.given()
                .get("/api/2")
                .then()
                .statusCode(404);
    }

    @Order(5)
    @Test
    void should_throw_exception_when_request_update_application_with_invalid_data() {
        String invalidRequestDto = """
                {
                        "firstname": "f1",
                        "lastname": "lastname",
                        "cellPhone": "1111111111",
                        "email": "email@email.com",
                        "hopeAmount": 1000.00
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(invalidRequestDto)
                .put("/api/1")
                .then()
                .statusCode(400)
                .body("data.firstname", equalTo("First name must contain only letters"));
    }

    @Order(6)
    @Test
    void should_update_application() {
        String requestDto = """
                {
                        "firstname": "update",
                        "lastname": "lastname",
                        "cellPhone": "1111111111",
                        "email": "email@email.com",
                        "hopeAmount": 1000.00
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .put("/api/1")
                .then()
                .log().all()
                .statusCode(200);

        RestAssured.given()
                .accept("application/json")
                .get("/api/1")
                .then()
                .statusCode(200)
                .body("data.firstname", equalTo("update"));

    }

    @Order(7)
    @Test
    void should_throw_exception_when_request_update_non_exist_application() {
        String requestDto = """
                {
                        "firstname": "firstname",
                        "lastname": "lastname",
                        "cellPhone": "1111111111",
                        "email": "email@email.com",
                        "hopeAmount": 1000.00
                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .put("/api/2")
                .then()
                .statusCode(404);
    }

    @Order(8)
    @Test
    void should_accept_terms() throws JsonProcessingException {
        Long applicationId = 1L;
        String requestDto = """
                {
                    "applicationId": 1,
                    "termsIds": [1]
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper();
        AcceptTermsRequestDto acceptTermsRequestDto = objectMapper.readValue(requestDto, AcceptTermsRequestDto.class);

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/api/" + applicationId + "/terms")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(9)
    @Test
    void should_update_grant() {
        Long applicationId = 1L;
        GrantAmountDto grantAmountDto = GrantAmountDto.builder()
                .applicationId(applicationId)
                .approvalAmount(new BigDecimal("1000.00"))
                .build();

        String requestJson = "{"
                + "\"applicationId\": " + grantAmountDto.getApplicationId() + ","
                + "\"approvalAmount\": " + grantAmountDto.getApprovalAmount()
                + "}";

        RestAssured.given()
                .contentType("application/json")
                .body(requestJson)
                .put("/api/" + applicationId + "/grant")
                .then()
                .log().all()
                .statusCode(200)
                .contentType("application/json");
    }

    @Order(10)
    @Test
    void should_contract_application() {
        Long applicationId = 1L;
        RestAssured.given()
                .contentType("application/json")
                .put("/api/" + applicationId + "/contract")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(11)
    @Test
    void should_delete_application() {
        RestAssured.given()
                .delete("/api/1")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(12)
    @Test
    void should_throw_exception_when_request_delete_non_exist_application() {
        RestAssured.given()
                .delete("/api/2")
                .then()
                .statusCode(404);
    }

    @Test
    void should_throw_exception_when_request_post_application_with_invalid_data() {
        String requestDto = """
                {
                        "firstname": "a1",
                        "lastname": "lastname",
                        "cellPhone": "1111111111",
                        "email": "email@email.com",
                        "hopeAmount": 1000.00
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/api")
                .then()
                .statusCode(400)
                .body("data.firstname", equalTo("First name must contain only letters"));
    }

}
