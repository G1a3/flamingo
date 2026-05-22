package com.flamingo.qa.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;

public class ApiClient {

    protected static final String BOOKER_BASE_URI = "https://restful-booker.herokuapp.com";

    static {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .jackson2ObjectMapperFactory((type, charset) -> {
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new JavaTimeModule());
                            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                            return mapper;
                        }));
    }

    protected static RequestSpecBuilder buildRequestSpec() {
        return buildPlainRequestSpec()
                .addFilter(new AllureRestAssured());
    }

    protected static RequestSpecBuilder buildPlainRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON);
    }
}