package com.example.applicationserver;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.dto.AcceptTermsRequestDto;
import com.example.applicationserver.dto.GrantAmountDto;
import com.example.applicationserver.stubs.AcceptTermsStub;
import com.example.applicationserver.stubs.FileStorageStub;
import com.example.applicationserver.stubs.JudgementStub;
import com.example.applicationserver.stubs.TermsStub;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@AutoConfigureWireMock(port = 0)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationServerApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
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
                .statusCode(200)
                .body("data.firstname", equalTo("firstname"));
    }

    @Order(3)
    @Test
    void should_throw_exception_when_request_non_exist_application_id() {
        RestAssured.given()
                .get("/api/2")
                .then()
                .statusCode(404);
    }

    @Order(4)
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

    @Order(5)
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
                .statusCode(200);

        RestAssured.given()
                .accept("application/json")
                .get("/api/1")
                .then()
                .statusCode(200)
                .body("data.firstname", equalTo("update"));

    }

    @Order(6)
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

    @Order(7)
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

        TermsStub.stubGetAllTerms();
        AcceptTermsStub.stubCreateAcceptTerms(acceptTermsRequestDto);


        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/api/" + applicationId + "/terms")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(8)
    @Test
    void should_upload_file() throws IOException {
        Long applicationId = 1L;
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is a test file".getBytes()
        );

        FileStorageStub.stubUploadFile(applicationId, file);

        RestAssured.given()
                .multiPart("file", file.getOriginalFilename(), file.getBytes(), MediaType.TEXT_PLAIN_VALUE)
                .contentType("multipart/form-data")
                .post("/api/" + applicationId + "/files")
                .then()
                .log().all()
                .statusCode(200);
    }


    @Order(9)
    @Test
    void should_download_file() {
        String fileName = "test.txt";
        String fileContent = "This is a test file";

        FileStorageStub.stubDownloadFile(1L, fileName);

        RestAssured.given()
                .queryParam("fileName", fileName)
                .get("/api/1/files")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(10)
    @Test
    void should_get_files_info() {
        Long applicationId = 1L;

        FileStorageStub.stubGetFilesInfo(applicationId);

        RestAssured.given()
                .get("/api/" + applicationId + "/files/info")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(11)
    @Test
    void should_delete_all_files() {
        Long applicationId = 1L;

        FileStorageStub.stubDeleteAllFiles(applicationId);

        RestAssured.given()
                .delete("/api/" + applicationId + "/files")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(12)
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

    @Order(13)
    @Test
    void should_contract_application() {
        Long applicationId = 1L;

        JudgementStub.stubGetJudgmentOfApplication(applicationId);

        RestAssured.given()
                .contentType("application/json")
                .put("/api/" + applicationId + "/contract")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Order(14)
    @Test
    void should_delete_application() {
        RestAssured.given()
                .delete("/api/1")
                .then()
                .statusCode(200);
    }

    @Order(15)
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
