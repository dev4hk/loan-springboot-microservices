package com.example.termsserver;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TermsServerApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Order(1)
    @Test
    void should_create_terms() {
        String requestDto = """
                {
                        "name": "name",
                        "termsDetailUrl": "https://randomTermsPage"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/terms")
                .then()
                .statusCode(200)
                .body("data.name", equalTo("name"));
    }

    @Order(2)
    @Test
    void should_throw_exception_when_request_post_terms_with_invalid_data() {
        String requestDto = """
                {
                        "name": null,
                        "termsDetailUrl": "https://randomTermsPage"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("/terms")
                .then()
                .statusCode(400)
                .body("data.name", equalTo("Name cannot be empty"));
    }

    @Order(3)
    @Test
    void should_get_terms_list() {
        RestAssured.given()
                .get("/terms")
                .then()
                .statusCode(200)
                .body("data[0].name", equalTo("name"))
                .body("data.size()", equalTo(1));
    }

    @Order(4)
    @Test
    void should_get_terms() {
        RestAssured.given()
                .get("/terms/1")
                .then()
                .statusCode(200)
                .body("data.name", equalTo("name"));
    }

    @Order(5)
    @Test
    void should_throw_exception_when_request_non_exist_terms_id() {
        RestAssured.given()
                .get("/terms/2")
                .then()
                .statusCode(404);
    }

    @Order(6)
    @Test
    void should_update_terms() {
        String requestDto = """
                {
                        "name": "update",
                        "termsDetailUrl": "https://randomTermsPage/update"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .put("/terms/1")
                .then()
                .statusCode(200)
                .body("data.name", equalTo("update"));
    }

    @Order(7)
    @Test
    void should_throw_exception_when_request_update_non_exist_terms() {
        String requestDto = """
                {
                        "name": "update",
                        "termsDetailUrl": "https://randomTermsPage/update"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .put("/terms/2")
                .then()
                .statusCode(404);
    }

    @Order(8)
    @Test
    void should_throw_exception_when_request_update_terms_with_invalid_data() {
        String invalidRequestDto = """
                {
                        "name": null,
                        "termsDetailUrl": "https://randomTermsPage/update"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(invalidRequestDto)
                .put("/terms/1")
                .then()
                .statusCode(400)
                .body("data.name", equalTo("Name cannot be empty"));
    }

    @Order(9)
    @Test
    void should_delete_terms() {
        RestAssured.given()
                .delete("/terms/1")
                .then()
                .statusCode(200);
    }

    @Order(10)
    @Test
    void should_throw_exception_when_request_delete_non_exist_terms() {
        RestAssured.given()
                .delete("/terms/2")
                .then()
                .statusCode(404);
    }

}
