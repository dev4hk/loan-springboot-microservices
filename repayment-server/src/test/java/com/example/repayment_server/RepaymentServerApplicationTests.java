package com.example.repayment_server;

import com.example.repayment_server.client.dto.BalanceRepaymentRequestDto;
import com.example.repayment_server.dto.RepaymentRequestDto;
import com.example.repayment_server.stubs.ApplicationStub;
import com.example.repayment_server.stubs.BalanceStub;
import com.example.repayment_server.stubs.EntryStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepaymentServerApplicationTests {

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}

	@Order(1)
	@Test
	void should_create_repayment() {
		Long applicationId = 1L;

		RepaymentRequestDto request = RepaymentRequestDto.builder()
				.repaymentAmount(BigDecimal.valueOf(1000.00))
				.build();

		ApplicationStub.stubApplicationGetCallContracted(applicationId);
		BalanceStub.stubUpdateBalance(
				applicationId,
				BalanceRepaymentRequestDto.builder()
						.repaymentAmount(BigDecimal.valueOf(1000.00))
						.type(BalanceRepaymentRequestDto.RepaymentType.REMOVE)
						.build()
		);
		EntryStub.stubGetEntry(applicationId);

		RestAssured.given()
				.contentType("application/json")
				.body(request)
				.post("/repayments/" + applicationId)
				.then()
				.log().all()
				.statusCode(200)
				.body("data.repaymentAmount", equalTo(1000.00F));
	}

	@Order(2)
	@Test
	void should_get_repayments() {
		Long applicationId = 1L;

		ApplicationStub.stubApplicationGetCallContracted(applicationId);

		RestAssured.given()
				.contentType("application/json")
				.get("/repayments/" + applicationId)
				.then()
				.log().all()
				.statusCode(200)
				.body("data[0].repaymentId", equalTo(1));
	}

	@Order(3)
	@Test
	void should_update_repayment() {
		Long repaymentId = 1L;
		Long applicationId = 1L;

		RepaymentRequestDto request = RepaymentRequestDto.builder()
				.repaymentAmount(BigDecimal.valueOf(2000.00))
				.build();

		BalanceStub.stubUpdateBalance(
				applicationId,
				BalanceRepaymentRequestDto.builder()
						.repaymentAmount(BigDecimal.valueOf(1000.00))
						.type(BalanceRepaymentRequestDto.RepaymentType.ADD)
						.build()
		);

		BalanceStub.stubUpdateBalance(
				applicationId,
				BalanceRepaymentRequestDto.builder()
						.repaymentAmount(BigDecimal.valueOf(2000.00))
						.type(BalanceRepaymentRequestDto.RepaymentType.REMOVE)
						.build()
		);

		RestAssured.given()
				.contentType("application/json")
				.body(request)
				.put("/repayments/" + repaymentId)
				.then()
				.log().all()
				.statusCode(200)
				.body("data.applicationId", equalTo(1))
				.body("data.afterRepaymentAmount", equalTo(2000.00F));
	}

	@Order(4)
	@Test
	void should_delete_repayment() {
		Long repaymentId = 1L;
		Long applicationId = 1L;

		BalanceStub.stubUpdateBalance(
				applicationId,
				BalanceRepaymentRequestDto.builder()
						.repaymentAmount(BigDecimal.valueOf(2000.00))
						.type(BalanceRepaymentRequestDto.RepaymentType.ADD)
						.build()
		);

		RestAssured.given()
				.contentType("application/json")
				.delete("/repayments/" + repaymentId)
				.then()
				.log().all()
				.statusCode(200);
	}

	@Order(5)
	@Test
	void should_fail_to_create_repayment_with_missing_repaymentAmount() {
		Long applicationId = 1L;

		RepaymentRequestDto request = RepaymentRequestDto.builder().build();

		ApplicationStub.stubApplicationGetCallContracted(applicationId);

		RestAssured.given()
				.contentType("application/json")
				.body(request)
				.post("/repayments/" + applicationId)
				.then()
				.log().all()
				.statusCode(400);
	}

	@Order(6)
	@Test
	void should_fail_to_update_repayment_with_invalid_repaymentAmount() {
		Long repaymentId = 1L;
		Long applicationId = 1L;

		RepaymentRequestDto request = RepaymentRequestDto.builder()
				.repaymentAmount(BigDecimal.valueOf(-2000.003))
				.build();

		BalanceStub.stubUpdateBalance(
				applicationId,
				BalanceRepaymentRequestDto.builder()
						.repaymentAmount(BigDecimal.valueOf(1000.00))
						.type(BalanceRepaymentRequestDto.RepaymentType.ADD)
						.build()
		);

		RestAssured.given()
				.contentType("application/json")
				.body(request)
				.put("/repayments/" + repaymentId)
				.then()
				.log().all()
				.statusCode(400);
	}

	@Order(7)
	@Test
	void should_fail_to_delete_nonexistent_repayment() {
		Long repaymentId = 999L;
		Long applicationId = 1L;

		BalanceStub.stubUpdateBalance(
				applicationId,
				BalanceRepaymentRequestDto.builder()
						.repaymentAmount(BigDecimal.valueOf(2000.00))
						.type(BalanceRepaymentRequestDto.RepaymentType.ADD)
						.build()
		);

		RestAssured.given()
				.contentType("application/json")
				.delete("/repayments/" + repaymentId)
				.then()
				.log().all()
				.statusCode(404);
	}
}
