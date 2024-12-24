package com.example.counselserver;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CounselServerApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void createCounsel() {
        String requestDto = """
                {
                    "name": "Random Name",
                    "cellPhone": "1111111111",
                    "email": "email@email.com",
                    "memo": "random memo",
                    "address": "random address",
                    "addressDetail": "11111",
                    "zipCode": "11111"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/counsels")
                .then()
                .statusCode(200);
    }
}
