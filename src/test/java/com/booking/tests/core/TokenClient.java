package com.booking.tests.core;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TokenClient {

    private static String baseUrl;
    private static String username;
    private static String password;

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(fis);
            baseUrl = prop.getProperty("base.url");
            username = prop.getProperty("auth.username");
            password = prop.getProperty("auth.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)   // RestAssured converts Map to JSON automatically
                .post(baseUrl + "/auth");

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to get token! Status code: " + response.getStatusCode());
        }

        return response.jsonPath().getString("token");
    }
}
