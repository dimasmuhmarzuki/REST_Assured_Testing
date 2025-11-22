package com.praktikum.rest.tests;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

/**
 * Test class untuk authentication related API endpoints
 * Menggunakan mock tests karena JSONPlaceholder tidak punya auth endpoints
 */
public class AuthenticationTests extends BaseTest {

    /**
     * Setup method untuk memastikan menggunakan API yang tepat
     */
    @BeforeMethod
    public void setupMethod() {
        useJSONPlaceholderAPI(); // Gunakan JSONPlaceholder untuk consistency
    }

    //---------------------------------------------------------
    // MOCK AUTHENTICATION TESTS (menggunakan /users/1)
    //---------------------------------------------------------

    /**
     * Mock test untuk simulate successful authentication
     * Menggunakan existing endpoint sebagai proxy untuk auth
     */
    @Test
    public void testSuccessfulLoginMock() {
        // Karena JSONPlaceholder tidak punya login endpoint,
        // kita menggunakan GET user sebagai mock login.
        given()
                .contentType(ContentType.JSON)
                .when()
                // GET existing user sebagai mock login
                .get("/users/1")
                .then()
                // Validate successful response
                .statusCode(200)
                .contentType(ContentType.JSON)
                // Validate JSON response data returned
                .body("id", equalTo(1)) // Validate user ID
                .body("name", not(emptyOrNullString())) // Validate name exists
                .body("email", not(emptyOrNullString())); // Validate email exists
    }

    /**
     * Mock test untuk simulate failed authentication
     * Menggunakan non-existent endpoint untuk simulate auth failure
     */
    @Test
    public void testFailedAuthenticationMock() {
        given()
                .contentType(ContentType.JSON)
                .when()
                // Non-existent endpoint untuk mock auth failure
                .get("/nonexistent-endpoint")
                .then()
                // Validate Not Found response
                .statusCode(404);
    }

    //---------------------------------------------------------
    // RESOURCE CREATION WITHOUT AUTH
    //---------------------------------------------------------

    /**
     * Test untuk create resource tanpa authentication
     * Validates: API behavior tanpa auth credentials
     */
    @Test
    public void testCreatePostWithoutAuth() {
        String postBody = """
        {
            "title": "Test Post",
            "body": "This is a test post without authentication",
            "userId": 1
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(postBody)
                .when()
                // POST request tanpa auth
                .post("/posts")
                .then()
                // Validate resource created
                .statusCode(201)
                .body("title", equalTo("Test Post")) // Validate title
                .body("body", containsString("test post")) // Validate body content
                .body("userId", equalTo(1)) // Validate user ID
                .body("id", notNullValue()); // Validate ID generated
    }

    //---------------------------------------------------------
    // ERROR HANDLING & DATA VALIDATION
    //---------------------------------------------------------

    /**
     * Test untuk simulate request dengan malformed data
     * Validates: API error handling untuk invalid requests
     */
    @Test
    public void testWithMalformedData() {
        given()
                .contentType(ContentType.JSON)
                // Malformed atau incomplete data
                .body("{\"invalid\": \"data\"}")
                .when()
                // POST request dengan invalid data
                .post("/posts")
                .then()
                // JSONPlaceholder masih accept data apapun
                .statusCode(201)
                // Masih generate ID
                .body("id", notNullValue());
    }

    //---------------------------------------------------------
    // OPTIONAL CHECK & HEADER VALIDATION
    //---------------------------------------------------------

    /**
     * Optional test untuk check ReqRes API availability
     * Dengan error handling dengan fungsi skip jika API down
     */
    @Test
    public void testReqResAPIAvailability() {
        try {
            // Switch sementara ke ReqRes API
            useReqresAPI();
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    // GET request ke ReqRes API
                    .get("/users")
                    .then()
                    // Accept multiple status codes
                    .statusCode(anyOf(equalTo(200), equalTo(403), equalTo(404)));
        } finally {
            // Always return to JSONPlaceholder untuk test consistency
            useJSONPlaceholderAPI();
        }
    }

    /**
     * Test untuk validate response headers
     * Important untuk security dan content negotiation
     */
    @Test
    public void testResponseHeaders() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                // Validate content type
                .header("Content-Type", containsString("application/json"))
                // Validate server header
                .header("Server", not(emptyOrNullString()))
                // Validate cache header
                .header("Cache-Control", not(emptyOrNullString()));
    }
}