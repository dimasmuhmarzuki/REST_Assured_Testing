package com.praktikum. rest. config;
/*** Configuration class untuk menyimpan semua constants dan configuration values
 * Digunakan oleh semua test classes untuk menjaga consistency*/
public class TestConfig {

    // URL Dasar untuk pengujian API - menggunakan JSONPlaceholder
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // URL API Alternatif untuk pengujian yang membutuhkan autentikasi (opsional)
    public static final String REQRES_BASE_URL = "https://reqres.in/api";

    // Kunci API untuk ReqRes API (jika diperlukan)
    public static final String API_KEY = "reqres-free-v1";
    public static final String API_KEY_HEADER = "X-API-Key";

    // Konstanta data uji untuk test data yang konsisten
    public static final String VALID_EMAIL = "eve.holt@reqres.in";
    public static final String VALID_PASSWORD = "cityslicka";
    public static final String INVALID_EMAIL = "invalid@test.com";

    // Ambang batas waktu respons dalam milidetik untuk pengujian kinerja

    // Maksimum waktu respons yang dapat diterima
    public static final long MAX_RESPONSE_TIME = 3000L;

    // Waktu respons yang ideal
    public static final long ACCEPTABLE_RESPONSE_TIME = 1000L;

    // Jalur (path) data untuk validasi skema JSON (jika digunakan)
    public static final String USERS_SCHEMA_PATH = "schemas/users-schema.json";
    public static final String USER_SCHEMA_PATH = "schemas/user-schema.json";
    public static final String LOGIN_SCHEMA_PATH = "schemas/login.json";
}