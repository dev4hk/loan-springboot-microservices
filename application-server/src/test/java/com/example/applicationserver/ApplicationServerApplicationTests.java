package com.example.applicationserver;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.equalTo;

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
                .post("/applications")
                .then()
                .statusCode(200)
                .body("data.firstname", equalTo("firstname"));
    }

    @Order(2)
    @Test
    void should_get_application() {
        RestAssured.given()
                .get("/applications/1")
                .then()
                .statusCode(200)
                .body("data.firstname", equalTo("firstname"));
    }

    @Order(3)
    @Test
    void should_throw_exception_when_request_non_exist_application_id() {
        RestAssured.given()
                .get("/applications/2")
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
                .put("/applications/1")
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
                .put("/applications/1")
                .then()
                .statusCode(200);

        RestAssured.given()
                .accept("application/json")
                .get("/applications/1")
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
                .put("/applications/2")
                .then()
                .statusCode(404);
    }

    @Order(7)
    @Test
    void should_delete_application() {
        RestAssured.given()
                .delete("/applications/1")
                .then()
                .statusCode(200);
    }

    @Order(8)
    @Test
    void should_throw_exception_when_request_delete_non_exist_application() {
        RestAssured.given()
                .delete("/applications/2")
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
                .post("/applications")
                .then()
                .statusCode(400)
                .body("data.firstname", equalTo("First name must contain only letters"));
    }


}
