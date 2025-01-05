package com.example.accepttermsserver;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptTermsServerApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Order(1)
    @Test
    void should_create_accept_terms() {
        String requestDto = """
                {
                    "applicationId": 1,
                    "termsIds": [1]
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/api")
                .then()
                .statusCode(200);
    }

    @Order(2)
    @Test
    void should_throw_exception_when_request_with_empty_terms_ids() {
        String requestDto = """
                {
                    "applicationId": 1,
                    "termsIds": []
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/api")
                .then()
                .statusCode(400);
    }

    @Order(3)
    @Test
    void should_throw_exception_when_request_with_existing_application_id_and_terms_ids() {
        String requestDto = """
                {
                    "applicationId": 1,
                    "termsIds": [1]
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/api")
                .then()
                .statusCode(400);
    }
}
