package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiUtils {

    public static final String BASE_URI = "https://bookstore.toolsqa.com";
    public static final String POST_USER_ENDPOINT = "/Account/v1/User";
    public static final String GENERATE_TOKEN_ENDPOINT = "/Account/v1/GenerateToken";
    public static final String POST_BOOKS_ENDPOINT = "/BookStore/v1/Books";
    private static Persona persona;

    // Set Persona for payload creation
    public static void setPersona(Persona p){
        persona = p;
    }

    // Get RestAssured request specification
    public static RequestSpecification getRequestSpec(){
        return RestAssured.given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json");
    }

    // Generic POST request
    public static Response post(String endpoint, Object body){
        return getRequestSpec()
                .body(body)
                .post(endpoint);
    }

    // GET request
    public static Response get(String endpoint){
        return getRequestSpec()
                .get(endpoint);
    }

    // GET request with Authentication
    public static Response getWithAuth(String endpoint, String token){
        return getRequestSpec()
                .header("Authorization", "Bearer " + token)
                .get(endpoint);
    }

    // DELETE request with authentication
    public static Response deleteWithAuth(String endpoint, String token){
        return getRequestSpec()
                .header("Authorization", "Bearer " + token)
                .delete(endpoint);
    }

    public static Map<String, String> createUserPayload() {
        Map<String, String> userPayload = new HashMap<>();
        userPayload.put("userName", persona.getUsername());
        userPayload.put("password", persona.getPassword());
        return userPayload;
    }

    public static Map<String, Object> createBookPayload(String isbn){
        Map<String, Object> bookPayload = new HashMap<>();
        bookPayload.put("userId", persona.getUserId());

        Map<String, String> isbnMap = new HashMap<>();
        isbnMap.put("isbn", isbn);

        List<Map<String, String>> collectionOfIsbns = new ArrayList<>();
        collectionOfIsbns.add(isbnMap);

        bookPayload.put("collectionOfIsbns", collectionOfIsbns);

        return bookPayload;
    }

    // Register a user and set credentials
    public static void registerUser(){
        Response response = post(POST_USER_ENDPOINT, createUserPayload());
        persona.setUserId(response.jsonPath().getString("userID"));

        response = post(GENERATE_TOKEN_ENDPOINT, createUserPayload());
        persona.setToken(response.jsonPath().getString("token"));
    }

    // Order a book
    public static Response orderBook(String isbn){
        return post(POST_BOOKS_ENDPOINT, createBookPayload(isbn));
    }

    // Get user's collection
    public static Response getUserCollection(String userId, String token){
        return getWithAuth(POST_USER_ENDPOINT + userId, token);
    }

    // Delete a user
    public static Response deleteUser(String userId, String token){
        return deleteWithAuth(POST_USER_ENDPOINT + userId, token);
    }

}
