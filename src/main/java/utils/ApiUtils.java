package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.PojoPersona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiUtils {

    private static PojoPersona persona;

    public static final String BASE_URI = "https://bookstore.toolsqa.com";
    public static final String POST_USER_ENDPOINT = "/Account/v1/User";
    public static final String GENERATE_TOKEN_ENDPOINT = "/Account/v1/GenerateToken";
    public static final String POST_BOOKS_ENDPOINT = "/BookStore/v1/Books";

    public static RequestSpecification getRequestSpec(){
        return RestAssured.given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json");
    }

    public static Response post(String endpoint, Object body){
        return getRequestSpec()
                .body(body)
                .post(endpoint);
    }

    public static Response get(String endpoint){
        return getRequestSpec().get(endpoint);
    }

    public static Response getWithAuth(String endpoint, String token){
        return getRequestSpec()
                .header("Authorization", "Bearer" + token)
                .get(endpoint);
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

}
