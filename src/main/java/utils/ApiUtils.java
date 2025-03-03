package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtils {

    private static final String BASE_URI = "https://bookstore.toolsqa.com";

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


}
