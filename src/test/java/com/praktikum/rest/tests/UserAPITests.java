package com.praktikum.rest.tests;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

/**
 * Test class untuk basic CRUD operations pada User API
 * Mengcover GET, POST, PUT, DELETE methods
 */
public class UserAPITests extends BaseTest {

    /**
     * Setup method yang di-execute sebelum setiap test method
     * Memastikan menggunakan JSONPlaceholder API untuk consistency
     */
    @BeforeMethod
    public void setupMethod() {
        useJSONPlaceholderAPI(); // Pastikan menggunakan JSONPlaceholder API
    }

    //---------------------------------------------------------
    // TEST GET (Read)
    //---------------------------------------------------------

    /**
     * Test untuk GET all users endpoint
     * Validates: status code, response structure, dan data completeness
     */
    @Test
    public void testGetAllUsers() {
        // Start building the request
        given()
                // Set content type sebagai JSON
                .contentType(ContentType.JSON)
                .when()
                // GET request ke /users endpoint
                .get("/users")
                .then()
                // Start response validation
                // Validate status code is 200 OK
                .statusCode(200)
                // Validate content type is JSON
                .contentType(ContentType.JSON)
                // Validate response array tidak empty
                .body("size()", greaterThan(0))
                // Validate first user has ID
                .body("[0].id", notNullValue())
                // Validate name exists
                .body("[0].name", not(emptyOrNullString()))
                // Validate email exists
                .body("[0].email", not(emptyOrNullString()))
                // Validate username exists
                .body("[0].username", not(emptyOrNullString()));
    }

    /**
     * Test untuk GET user by ID endpoint
     * Validates: specific user data, field values, dan response structure
     */
    @Test
    public void testGetUserById() {
        given()
                .contentType(ContentType.JSON)
                // Set path parameter {id} dengan value 1
                .pathParam("id", 1)
                .when()
                // Specify the HTTP action
                // GET request ke /users/{id}
                .get("/users/{id}")
                .then()
                // Validate status code
                .statusCode(200)
                // Validate content type
                .contentType(ContentType.JSON)
                // Validate user ID adalah 1
                .body("id", equalTo(1))
                // Validate nama user
                .body("name", equalTo("Leanne Graham"))
                // Validate email user
                .body("email", equalTo("Sincere@april.biz"))
                // Validate username
                .body("username", equalTo("Bret"));
    }

    /**
     * Test untuk GET non-existent user
     * Validates: error handling untuk resource yang tidak ada
     */
    @Test
    public void testGetUserNotFound() {
        given()
                .contentType(ContentType.JSON)
                // Use non-existent user ID
                .pathParam("id", 999)
                .when()
                // GET request ke /users/999
                .get("/users/{id}")
                .then()
                // Validate status code 404 Not Found
                .statusCode(404);
    }

    //---------------------------------------------------------
    // TEST POST (Create)
    //---------------------------------------------------------

    /**
     * Test untuk POST create new user endpoint
     * Validates: resource creation, response data, dan field values
     */
    @Test
    public void testCreateUser() {
        // Request body sebagai JSON string
        String requestBody = """
        {
            "name": "John Doe",
            "username": "johndoe",
            "email": "john.doe@example.com",
            "address": {
                "street": "123 Main St",
                "city": "Anytown"
            },
            "phone": "1-555-123-4567",
            "website": "johndoe.com",
            "company": {
                "name": "ABC Company",
                "catchPhrase": "Best Company ever"
            }
        }
        """;

        given()
                .contentType(ContentType.JSON)
                // Set request body
                .body(requestBody)
                .when()
                // POST request ke /users endpoint
                .post("/users")
                .then()
                // Validate status 201 Created
                .statusCode(201)
                .contentType(ContentType.JSON)
                // Validate nama dalam response
                .body("name", equalTo("John Doe"))
                // Validate username dalam response
                .body("username", equalTo("johndoe"))
                // Validate email dalam response
                .body("email", equalTo("john.doe@example.com"))
                // Validate ID generated oleh server
                .body("id", notNullValue());
    }

    //---------------------------------------------------------
    // TEST PUT (Update)
    //---------------------------------------------------------

    /**
     * Test untuk PUT update user endpoint
     * Validates: resource update, field changes, dan response data
     */
    @Test
    public void testUpdateUser() {
        // Request body dengan updated data
        String requestBody = """
        {
            "name": "John Updated",
            "username": "johnupdated",
            "email": "john.updated@example.com"
        }
        """;

        given()
                .contentType(ContentType.JSON)
                // User ID yang akan di-update
                .pathParam("id", 1)
                // Updated data
                .body(requestBody)
                .when()
                // PUT request ke /users/1
                .put("/users/{id}")
                .then()
                // Validate status code 200 OK
                .statusCode(200)
                .contentType(ContentType.JSON)
                // Validate nama terupdate
                .body("name", equalTo("John Updated"))
                // Validate username terupdate
                .body("username", equalTo("johnupdated"))
                // Validate email terupdate
                .body("email", equalTo("john.updated@example.com"))
                // Validate ID tetap sama
                .body("id", equalTo(1));
    }

    //---------------------------------------------------------
    // TEST DELETE (Delete)
    //---------------------------------------------------------

    /**
     * Test untuk DELETE user endpoint
     * Validates: resource deletion dan response structure
     */
    @Test
    public void testDeleteUser() {
        given()
                .contentType(ContentType.JSON)
                // User ID yang akan di-delete
                .pathParam("id", 1)
                .when()
                // DELETE request ke /users/1
                .delete("/users/{id}")
                .then()
                // Validate status code 200 OK
                .statusCode(200);
    }
}