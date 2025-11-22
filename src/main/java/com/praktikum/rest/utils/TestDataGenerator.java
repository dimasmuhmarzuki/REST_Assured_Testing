package com.praktikum.rest.utils;
import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Arrays; // Ditambahkan untuk Arrays.asList

/**
 * Utility class untuk generate realistic test data menggunakan Java Faker
 * Membantu membuat test data yang variatif dan realistic
 */
public class TestDataGenerator {

    // Initialize Faker dengan Indonesian locale untuk data yang lebih relatable
    private static final Faker faker = new Faker(new Locale("id-ID"));

    /**
     * Generate complete user data dengan semua fields
     * @return Map berisi user data dengan structure yang complete
     */
    public static Map<String, Object> generateUserData() {
        Map<String, Object> userData = new HashMap<>();

        // Basic user information
        userData.put("name", faker.name().fullName()); // Full name
        // Username tanpa special characters
        userData.put("username", faker.name().username().replaceAll("[^a-zA-Z0-9]", ""));
        userData.put("email", faker.internet().emailAddress()); // Email address
        userData.put("phone", faker.phoneNumber().phoneNumber()); // Phone number
        userData.put("website", faker.internet().url()); // Website URL

        // Address information
        Map<String, Object> address = new HashMap<>();
        address.put("street", faker.address().streetAddress()); // Street address
        address.put("city", faker.address().city()); // City
        address.put("zipcode", faker.address().zipCode()); // ZIP code
        userData.put("address", address);

        // Company information
        Map<String, Object> company = new HashMap<>();
        company.put("name", faker.company().name()); // Company name
        company.put("catchPhrase", faker.company().catchPhrase()); // Company catchphrase
        company.put("bs", faker.company().bs()); // Business statement
        userData.put("company", company);

        return userData;
    }

    /**
     * Generate user data dengan address information
     * @return Map berisi user data dengan address
     */
    public static Map<String, Object> generateUserWithAddress() {
        // Get basic user data
        Map<String, Object> userData = generateUserData();

        Map<String, Object> address = new HashMap<>();
        address.put("street", faker.address().streetAddress()); // Street
        address.put("city", faker.address().city()); // City
        address.put("zipcode", faker.address().zipCode()); // ZIP code

        // Add address to user data
        userData.put("address", address);

        return userData;
    }

    /**
     * Generate company data saja
     * @return Map berisi company data
     */
    public static Map<String, Object> generateCompanyData() {
        Map<String, Object> companyData = new HashMap<>();

        // Company name
        companyData.put("name", faker.company().name());
        // Company tagline
        companyData.put("catchPhrase", faker.company().catchPhrase());
        // Business statement
        companyData.put("bs", faker.company().bs());

        return companyData;
    }

    /**
     * Generate valid user data untuk positive testing
     * @return Map berisi valid user data
     */
    public static Map<String, Object> generateValidUserData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "Test User " + faker.number().digits(3)); // Test User dengan random digits
        // Username dengan random digits
        userData.put("username", "user" + faker.number().digits(3));
        // Valid email
        userData.put("email", faker.internet().emailAddress());
        return userData;
    }

    /**
     * Generate product data untuk e-commerce testing
     * @return Map berisi product data
     */
    public static Map<String, Object> generateProductData() {
        Map<String, Object> productData = new HashMap<>();
        // Product name
        productData.put("name", faker.commerce().productName());
        // Product price
        productData.put("price", faker.commerce().price());
        // Department
        productData.put("department", faker.commerce().department());
        // Material
        productData.put("material", faker.commerce().material());
        return productData;
    }

    /**
     * Generate login credentials (mock)
     * @return Map berisi email dan password
     */
    public static Map<String, Object> generateLoginData() {
        Map<String, Object> loginData = new HashMap<>();
        // Email
        loginData.put("email", faker.internet().emailAddress());
        // Strong password
        loginData.put("password", faker.internet().password(8, 12, true, true));
        return loginData;
    }

    /**
     * Generate multiple users untuk bulk testing
     * @param count Jumlah users yang akan di-generate
     * @return Array of user data maps
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object>[] generateMultipleUsers(int count) {
        Map<String, Object>[] users = new HashMap[count];
        for (int i = 0; i < count; i++) {
            // Generate untuk masing-masing user
            users[i] = generateUserData();
        }
        return users;
    }

    /**
     * Generate user data dalam format JSON string
     * @return JSON string dengan user data
     */
    public static String generateUserJson() {
        return String.format("""
        {
            "name": "%s",
            "username": "%s",
            "email": "%s"
        }
        """,
                faker.name().fullName(), // Name
                faker.name().username().replaceAll("[^a-zA-Z0-9]", ""), // Username
                faker.internet().emailAddress() // Email
        );
    }
}