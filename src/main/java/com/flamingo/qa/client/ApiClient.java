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

     /**
      * Configures REST Assured global settings,
      * Logs requests/responses on validation failures
      * cConfigures Jackson ObjectMapper to properly handle Java date/time types
     */
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

    // Adds an Allure filter to REST Assured, which captures HTTP request and response details and attaches them to the Allure report
    protected static RequestSpecBuilder buildRequestSpec() {
        return buildPlainRequestSpec()
                .addFilter(new AllureRestAssured());
    }

    // Builds an HTTP request template for REST Assured — common settings that are then applied to each API request
    protected static RequestSpecBuilder buildPlainRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON);
    }
}