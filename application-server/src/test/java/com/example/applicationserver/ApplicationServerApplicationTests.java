package com.example.applicationserver;

import com.example.applicationserver.client.AcceptTermsClient;
import com.example.applicationserver.client.FileStorageClient;
import com.example.applicationserver.client.JudgementClient;
import com.example.applicationserver.client.TermsClient;
import com.example.applicationserver.client.dto.*;
import com.example.applicationserver.dto.GrantAmountDto;
import com.example.applicationserver.dto.ResponseDTO;
import com.example.applicationserver.entity.Application;
import com.example.applicationserver.repository.ApplicationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationServerApplicationTests {

    @LocalServerPort
    private int port;

    @MockitoBean
    private ApplicationRepository applicationRepository;

    @MockitoBean
    private TermsClient termsClient;

    @MockitoBean
    private AcceptTermsClient acceptTermsClient;

    @MockitoBean
    private FileStorageClient fileStorageClient;

    @MockitoBean
    private JudgementClient judgementClient;

    private Application application;

    private TermsResponseDto termsResponseDto;

    private AcceptTermsResponseDto acceptTermsResponseDto;

    private FileResponseDto fileResponseDto;

    private JudgementResponseDto judgementResponseDto;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        application = Application.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .hopeAmount(BigDecimal.valueOf(5000))
                .build();

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

        fileResponseDto = FileResponseDto.builder()
                .name("filename")
                .url("url")
                .build();

        judgementResponseDto = JudgementResponseDto.builder()
                .applicationId(1L)
                .judgementId(1L)
                .approvalAmount(BigDecimal.valueOf(5000))
                .build();


        when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(applicationRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        when(termsClient.getAll()).thenReturn(new ResponseDTO<>(List.of(termsResponseDto)));
        when(acceptTermsClient.create(any(AcceptTermsRequestDto.class))).thenReturn(new ResponseDTO<>(List.of(acceptTermsResponseDto)));
        when(fileStorageClient.upload(anyLong(), any(MultipartFile.class))).thenReturn(new ResponseDTO<>());
        when(fileStorageClient.getFilesInfo(anyLong())).thenReturn(new ResponseDTO<>(List.of(fileResponseDto)));
        when(fileStorageClient.deleteAll(anyLong())).thenReturn(new ResponseDTO<>());

        byte[] fileContent = "This is a test file".getBytes();
        Resource resource = new ByteArrayResource(fileContent);
        when(fileStorageClient.download(anyLong(), anyString())).thenReturn(ResponseEntity.ok(resource));

        when(judgementClient.getJudgmentOfApplication(anyLong())).thenReturn(new ResponseDTO<>(judgementResponseDto));
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

    @Order(3)
    @Test
    void should_throw_exception_when_request_non_exist_application_id() {
        when(applicationRepository.findById(anyLong())).thenReturn(Optional.empty());
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
                .log().all()
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
        when(applicationRepository.findById(anyLong())).thenReturn(Optional.empty());
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
        application = Application.builder()
                .applicationId(1L)
                .firstname("firstname")
                .lastname("lastname")
                .hopeAmount(BigDecimal.valueOf(5000))
                .approvalAmount(BigDecimal.valueOf(5000))
                .build();
        when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
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
                .log().all()
                .statusCode(200);
    }

    @Order(15)
    @Test
    void should_throw_exception_when_request_delete_non_exist_application() {
        when(applicationRepository.findById(anyLong())).thenReturn(Optional.empty());
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
