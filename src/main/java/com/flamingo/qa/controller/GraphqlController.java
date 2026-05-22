package com.flamingo.qa.controller;

import com.flamingo.qa.client.HygraphApiClient;
import com.flamingo.qa.graphql.GraphqlRequest;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class GraphqlController extends HygraphApiClient {

    public Response post(String query) {
        return post(query, Map.of());
    }

    public Response post(String query, Map<String, Object> variables) {
        GraphqlRequest body = GraphqlRequest.builder()
                .query(query)
                .variables(variables)
                .build();
        return given()
                .spec(requestSpec())
                .body(body)
                .post()
                .then()
                .extract()
                .response();
    }
}
