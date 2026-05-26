package com.flamingo.qa.client;

import com.flamingo.qa.responses.AuthResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookerApiClient extends ApiClient {

    // Bad practice
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "password123";

    private static final String BASE_URI = BOOKER_BASE_URI;
    protected static ThreadLocal<RequestSpecification> reqSpec = new ThreadLocal<>();
    protected static ThreadLocal<RequestSpecification> authenticatedReqSpec = new ThreadLocal<>();

    public BookerApiClient() {
        if (reqSpec.get() == null) {
            configureApiClient();
        }
    }

    protected RequestSpecification requestSpec() {
        if (reqSpec.get() == null) {
            configureApiClient();
        }
        return reqSpec.get();
    }

    protected RequestSpecification authenticatedRequestSpec() {
        if (authenticatedReqSpec.get() == null) {
            configureAuthenticatedApiClient(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        }
        return authenticatedReqSpec.get();
    }

    protected static void configureApiClient() {
        reqSpec.set(buildRequestSpec()
                .setBaseUri(BASE_URI)
                .build());
    }

    protected static void configureAuthenticatedApiClient(String username, String password) {
        authenticatedReqSpec.set(authenticatedRequestSpec(username, password));
    }

    protected static RequestSpecification authenticatedRequestSpec(String username, String password) {
        AuthResponse authResponse = createAuthToken(username, password);
        return buildRequestSpec()
                .setBaseUri(BASE_URI)
                .addCookie("token", authResponse.getToken())
                .build();
    }

    protected static AuthResponse createAuthToken(String username, String password) {
        return given()
                .spec(buildRequestSpec().setBaseUri(BASE_URI).build())
                .body(Map.of(
                        "username", username,
                        "password", password
                ))
                .when()
                .post("/auth")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(AuthResponse.class);
    }
}
