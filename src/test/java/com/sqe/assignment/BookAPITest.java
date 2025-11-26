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
public class BookAPITest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	public void testCreateBook_ValidData() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"title\":\"New Book\",\"author\":\"Author A\",\"memberName\":\"John\"}")
				.when()
				.post("/books")
				.then()
				.statusCode(200)
				.body("title", equalTo("New Book"))
				.body("author", equalTo("Author A"))
				.body("memberName", equalTo("John"));
	}

	@Test
	public void testCreateBook_MissingTitle() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"author\":\"Author A\",\"memberName\":\"John\"}")
				.when()
				.post("/books")
				.then()
				.statusCode(400);
	}

	@Test
	public void testCreateBook_NonExistingMember() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"title\":\"Book X\",\"author\":\"Author B\",\"memberName\":\"NonExistent\"}")
				.when()
				.post("/books")
				.then()
				.statusCode(200)
				.body("title", equalTo("Book X"))
				.body("author", equalTo("Author B"))
				.body("memberName", anyOf(nullValue(), equalTo("NonExistent")));
	}

	@Test
	public void testCreateBook_Duplicate() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"title\":\"My Book\",\"author\":\"Author A\",\"memberName\":\"John\"}")
				.when()
				.post("/books");

		given()
				.contentType(ContentType.JSON)
				.body("{\"title\":\"My Book\",\"author\":\"Author A\",\"memberName\":\"John\"}")
				.when()
				.post("/books")
				.then()
				.statusCode(409); // Assuming your API returns 409 for duplicates
	}

	@Test
	public void testGetBook_ValidTitle() {
		get("/books/My Book")
				.then()
				.statusCode(200)
				.body("title", equalTo("My Book"));
	}

	@Test
	public void testGetBook_InvalidTitle() {
		get("/books/UnknownBook")
				.then()
				.statusCode(404);
	}

	@Test
	public void testUpdateBook_Valid() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"title\":\"My Book\",\"author\":\"Updated Author\",\"memberName\":\"John\"}")
				.when()
				.put("/books/My Book")
				.then()
				.statusCode(200)
				.body("author", equalTo("Updated Author"));
	}

	@Test
	public void testDeleteBook_Existing() {
		delete("/books/My Book")
				.then()
				.statusCode(anyOf(equalTo(200), equalTo(204)));
	}

	@Test
	public void testDeleteBook_NonExisting() {
		delete("/books/UnknownBook")
				.then()
				.statusCode(404);
	}
}
