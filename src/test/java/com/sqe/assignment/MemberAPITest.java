package com.sqe.assignment;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MemberAPITest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	public void testCreateMember_ValidData() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}")
				.when()
				.post("/members")
				.then()
				.statusCode(200)
				.body("name", equalTo("John Doe"))
				.body("email", equalTo("john@example.com"));
	}

	@Test
	public void testCreateMember_MissingEmail() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"name\":\"John Doe\"}")
				.when()
				.post("/members")
				.then()
				.statusCode(400);
	}

	@Test
	public void testCreateMember_DuplicateEmail() {
		// First, create a member
		given()
				.contentType(ContentType.JSON)
				.body("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}")
				.when()
				.post("/members");

		// Try to create the same member again
		given()
				.contentType(ContentType.JSON)
				.body("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}")
				.when()
				.post("/members")
				.then()
				.statusCode(409); // Conflict - duplicate email
	}

	@Test
	public void testGetMember_ValidEmail() {
		get("/members/john@example.com")
				.then()
				.statusCode(200)
				.body("name", equalTo("John Doe"))
				.body("email", equalTo("john@example.com"));
	}

	@Test
	public void testGetMember_InvalidEmail() {
		get("/members/unknown@example.com")
				.then()
				.statusCode(404);
	}

	@Test
	public void testUpdateMember_ValidData() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"name\":\"John Updated\",\"email\":\"john@example.com\"}")
				.when()
				.put("/members/john@example.com")
				.then()
				.statusCode(200)
				.body("name", equalTo("John Updated"))
				.body("email", equalTo("john@example.com"));
	}

	@Test
	public void testDeleteMember_Existing() {
		delete("/members/john@example.com")
				.then()
				.statusCode(anyOf(equalTo(200), equalTo(204)));
	}

	@Test
	public void testDeleteMember_NonExisting() {
		delete("/members/unknown@example.com")
				.then()
				.statusCode(404);
	}
}

