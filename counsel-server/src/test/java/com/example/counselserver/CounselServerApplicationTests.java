package com.example.counselserver;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CounselServerApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Order(1)
    @Test
    void should_create_counsel() {
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
                .statusCode(200)
                .body("data.name", equalTo("Random Name"));
    }

    @Order(2)
    @Test
    void should_get_counsel() {
        RestAssured.given()
                .get("/counsels/1")
                .then()
                .statusCode(200)
                .body("data.name", equalTo("Random Name"));
    }

    @Order(3)
    @Test
    void should_throw_exception_when_request_non_exist_counsel_id() {
        RestAssured.given()
                .get("/counsels/2")
                .then()
                .statusCode(404);
    }

    @Order(4)
    @Test
    void should_update_counsel() {
        String requestDto = """
                {
                    "name": "Update Name",
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
                .put("/counsels/1")
                .then()
                .statusCode(200);

        RestAssured.given()
                .accept("application/json")
                .get("/counsels/1")
                .then()
                .statusCode(200)
                .body("data.name", equalTo("Update Name"));

    }

    @Order(5)
    @Test
    void should_throw_exception_when_request_update_non_exist_counsel() {
        String requestDto = """
                {
                    "name": "Update Name",
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
                .put("/counsels/2")
                .then()
                .statusCode(404);
    }

    @Order(6)
    @Test
    void should_delete_counsel() {
        RestAssured.given()
                .delete("/counsels/1")
                .then()
                .statusCode(200);
    }

    @Order(7)
    @Test
    void should_throw_exception_when_request_delete_non_exist_counsel() {
        RestAssured.given()
                .delete("/counsels/2")
                .then()
                .statusCode(404);
    }

}
