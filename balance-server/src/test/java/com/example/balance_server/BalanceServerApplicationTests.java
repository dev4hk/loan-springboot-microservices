package com.example.balance_server;

import com.example.balance_server.repository.BalanceRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BalanceServerApplicationTests {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setup() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}

	@Order(1)
	@Test
	void should_create_balance() {
		String requestDto = """
                {
                        "applicationId": 1,
                        "entryAmount": 1000.00
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(requestDto)
				.post("/balances/1")
				.then()
				.log().all()
				.statusCode(200)
				.body("data.balance", equalTo(1000.00F));
	}

	@Order(2)
	@Test
	void should_get_balance() {
		RestAssured.given()
				.get("/balances/1")
				.then()
				.log().all()
				.statusCode(200)
				.body("data.balance", equalTo(1000.00F));
	}

	@Order(3)
	@Test
	void should_throw_exception_when_request_non_exist_balance_id() {
		RestAssured.given()
				.get("/balances/2")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Order(4)
	@Test
	void should_throw_exception_when_request_update_balance_with_invalid_data() {
		String invalidRequestDto = """
                {
                        "applicationId": 1,
                        "beforeEntryAmount": -1000.001,
                        "afterEntryAmount": 1000.00
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(invalidRequestDto)
				.put("/balances/1")
				.then()
				.log().all()
				.statusCode(400)
				.body("data.beforeEntryAmount", equalTo("Before entry amount must be a number with up to 2 decimal places"));
	}

	@Order(5)
	@Test
	void should_update_balance() {
		String requestDto = """
                {
                        "applicationId": 1,
                        "beforeEntryAmount": 1000.00,
                        "afterEntryAmount": 2000.00
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(requestDto)
				.put("/balances/1")
				.then()
				.log().all()
				.statusCode(200);

	}

	@Order(6)
	@Test
	void should_throw_exception_when_request_update_non_exist_balance() {
		String requestDto = """
                {
                        "applicationId": 2,
                        "beforeEntryAmount": 1000.00,
                        "afterEntryAmount": 2000.00
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(requestDto)
				.put("/balances/2")
				.then()
				.log().all()
				.statusCode(404);
	}


	@Test
	void should_throw_exception_when_request_post_balance_with_invalid_data() {
		String requestDto = """
                {
                        "applicationId": null,
                        "entryAmount": -1000.00
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(requestDto)
				.post("/balances/1")
				.then()
				.log().all()
				.statusCode(400);
	}

	@Order(7)
	@Test
	void should_repayment_update_balance() {
		String requestDto = """
                {
                        "type": "ADD",
                        "repaymentAmount": 500.00
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(requestDto)
				.patch("/balances/1/repayment")
				.then()
				.log().all()
				.statusCode(200)
				.body("data.balance", equalTo(2500.00F));
	}

	@Order(8)
	@Test
	void should_throw_exception_when_repayment_update_with_invalid_data() {
		String requestDto = """
                {
                        "type": null,
                        "repaymentAmount": -1000.00
                }
                """;

		RestAssured.given()
				.contentType("application/json")
				.body(requestDto)
				.patch("/balances/1/repayment")
				.then()
				.log().all()
				.statusCode(400);
	}

	@Order(9)
	@Test
	void should_delete_balance() {
		RestAssured.given()
				.delete("/balances/1")
				.then()
				.log().all()
				.statusCode(200);
	}

	@Order(10)
	@Test
	void should_throw_exception_when_request_delete_non_exist_balance() {
		RestAssured.given()
				.delete("/balances/2")
				.then()
				.log().all()
				.statusCode(404);
	}

}

