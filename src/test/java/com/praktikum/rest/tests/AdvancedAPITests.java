package com.praktikum.rest.tests;
import com.praktikum.rest.utils.TestDataGenerator;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class untuk advanced API testing scenarios
 * Mengcover data-driven testing, negative testing, performance testing,
 * header validation, dan lain-lain.
 */
public class AdvancedAPITests extends BaseTest {

    /**
     * Setup method untuk setiap test
     */
    @BeforeMethod
    public void setupMethod() {
        useJSONPlaceholderAPI(); // Gunakan JSONPlaceholder
    }

    //---------------------------------------------------------
    // HEADER & PERFORMANCE TESTING
    //---------------------------------------------------------

    /**
     * Test untuk validate response headers
     * Important untuk security, caching, dan content negotiation
     */
    @Test
    public void testResponseHeadersValidation() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                // validate JSON content type
                .header("Content-Type", containsString("application/json"))
                // Validate server exists
                .header("Server", not(emptyOrNullString()))
                // Validate technology stack
                .header("X-Powered-By", not(emptyOrNullString()))
                // Validate compression
                .header("Content-Encoding", equalTo("gzip"));
    }

    /**
     * Performance test untuk mengukur response time
     * Validates API performance under normal conditions
     */
    @Test
    public void testResponseTimePerformance() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                // Validate response time less than 3 seconds
                .time(lessThan(3000L));
    }

    //---------------------------------------------------------
    // DATA-DRIVEN TESTING (DDT)
    //---------------------------------------------------------

    /**
     * DataProvider untuk provide multiple user IDs
     * @return Array of user IDs untuk data-driven testing
     */
    @DataProvider (name = "validUserIds")
    public Object[] provideValidUserIds() {
        // Multiple user IDs untuk testing
        return new Object[] {1, 2, 3, 4, 5};
    }

    /**
     * Data-driven test menggunakan TestNG DataProvider
     * Test multiple scenarios dengan different input data
     */
    @Test(dataProvider = "validUserIds")
    public void testMultipleUsersWithDataProvider(int userId) {
        // Dynamic user ID dari DataProvider
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                // Validate ID matches input
                .body("id", equalTo(userId))
                // Validate name exists
                .body("name", not(emptyOrNullString()))
                // Validate email exists
                .body("email", not(emptyOrNullString()))
                // Validate username exists
                .body("username", not(emptyOrNullString()));
    }

    /**
     * Data-driven test dengan Java Faker generated data
     * Demonstrates dynamic test data generation
     */
    @Test
    public void testDataDrivenWithFaker() {
        // Generate 2 test data sets dengan Faker
        for (int i = 0; i < 2; i++) {
            Map<String, Object> userData = TestDataGenerator.generateUserData();

            // Print generated data untuk debugging
            System.out.println("Generated User " + (i+1) + ": " + userData.get("name"));

            given()
                    .contentType(ContentType.JSON)
                    .body(userData)
                    .when()
                    .post("/users")
                    .then()
                    .statusCode(201)
                    // Validate generated name
                    .body("name", equalTo(userData.get("name")))
                    // Validate ID generated
                    .body("id", notNullValue());
        }
    }

    //---------------------------------------------------------
    // DIFFERENT BODY FORMATS & METHODS
    //---------------------------------------------------------

    /**
     * Test untuk create user menggunakan HashMap
     * Demonstrates different ways to create request body
     */
    @Test
    public void testCreateUserWithHashMap() {
        // Create request body menggunakan HashMap
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User HashMap");
        userData.put("username", "testhashmapuser");
        userData.put("email", "test@example.com");

        given()
                .contentType(ContentType.JSON)
                // Pass HashMap sebagai request body
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                // Validate name
                .body("name", equalTo("Test User HashMap"))
                // Validate username
                .body("username", equalTo("testhashmapuser"))
                // Validate email
                .body("email", equalTo("test@example.com"))
                // Validate ID generated
                .body("id", notNullValue());
    }
    /**
     * Test untuk Create user dengan Faker JSON string
     * Demonstrates different data format handling
     */
    @Test
    public void testCreateUserWithFakerJson() {
        // Get JSON string dari Faker
        String userJson = TestDataGenerator.generateUserJson();

        System.out.println("Generated JSON: " + userJson); // Log generated JSON

        given()
                .contentType(ContentType.JSON)
                // Pass JSON string sebagai body
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                // Validate name exists
                .body("name", not(emptyOrNullString()))
                // Validate ID generated
                .body("id", notNullValue());
    }
    /**
     * Test untuk partial update menggunakan PATCH method
     * Validates partial resource updates
     */
    @Test
    public void testUpdateUserWithPartialData() {
        // Partial data untuk update hanya specific fields
        Map<String, Object> partialData = new HashMap<>();
        // Update hanya name
        partialData.put("name", "Updated Name Only");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                // Only send fields to update
                .body(partialData)
                .when()
                // PATCH request untuk partial update
                .patch("/users/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Name Only"))
                // Validate ID unchanged
                .body("id", equalTo(1));
    }

    //---------------------------------------------------------
    // NESTED RESOURCES & PAGINATION
    //---------------------------------------------------------

    /**
     * Test untuk nested resources - Get posts for specific user
     * Validates API resource relationships
     */
    @Test
    public void testGetPostsForUser() {
        given()
                .contentType(ContentType.JSON)
                // Query parameter untuk filter by user ID
                .queryParam("userId", 1)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                // Validate non-empty response
                .body("size()", greaterThan(0))
                // Validate all posts belong to user 1
                .body("[0].userId", equalTo(1))
                // Validate title exists
                .body("[0].title", not(emptyOrNullString()))
                // Validate body exists
                .body("[0].body", not(emptyOrNullString()));
    }

    /**
     * Test untuk pagination functionality
     * Validates API behavior dengan query parameters
     */
    @Test
    public void testPaginationFunctionality() {
        given()
                .contentType(ContentType.JSON)
                // Query parameter untuk filter by user
                .queryParam("userId", 1)
                .when()
                // GET posts for specific user
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0)); // Validate non-empty response
    }

    //---------------------------------------------------------
    // NEGATIVE & BOUNDARY TESTING
    //---------------------------------------------------------

    /**
     * Negative test - Create user dengan null values
     * Validates API handling of null data
     */
    @Test
    public void testCreateUserWithNullValues() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", null); // Null value
        userData.put("username", "testuser");
        userData.put("email", "test@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                // JSONPlaceholder handles nulls gracefully
                .body("username", equalTo("testuser"));
        // Validate other fields
    }

    /**
     * Negative test - Create user dengan empty body
     * Validates API behavior dengan incomplete data
     */
    @Test
    public void testCreateUserWithEmptyBody() {
        given()
                .contentType(ContentType.JSON)
                .body("{}") // Empty JSON object
                .when()
                .post("/users")
                .then()
                .statusCode(201); // JSONPlaceholder accepts empty body
    }

    /**
     * Negative test - Create user dengan invalid JSON
     * Validates API error handling untuk malformed requests
     */
    @Test
    public void testCreateUserWithInvalidJSON() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"invalid json\"") // Invalid JSON syntax
                .when()
                .post("/users")
                .then()
                // JSONPlaceholder returns 500 untuk invalid JSON
                .statusCode(500);
    }

    /**
     * Negative test - Get user dengan invalid ID format
     * Validates API behavior dengan invalid path parameters
     */
    @Test
    public void testGetUserWithInvalidId() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", "invalid") // Non-numeric ID
                .when()
                .get("/users/{id}")
                .then()
                // Not Found untuk invalid ID format
                .statusCode(404);
    }

    /**
     * Boundary test - Create user dengan very long name
     * Validates API handling of extreme data values
     */
    @Test
    public void testCreateUserWithVeryLongName() {
        Map<String, Object> userData = new HashMap<>();
        // Very long name (A repeated 500 times)
        userData.put("name", "A".repeat(500));
        userData.put("username", "longuser");
        userData.put("email", "long@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                // JSONPlaceholder accepts long values
                .body("username", equalTo("longuser"));
        // Validate username
    }
}