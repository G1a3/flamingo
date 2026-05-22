package com.flamingo.qa.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class HygraphApiClient extends ApiClient {

    public static final String HYGRAPH_VIDEO_STREAMING_URI =
            "https://us-east-1-shared-usea1-02.cdn.hygraph.com/content/clpvcopq3aavs01usft1idkgj/master";

    private static final ThreadLocal<RequestSpecification> reqSpec = new ThreadLocal<>();

    public HygraphApiClient() {
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

    protected static void configureApiClient() {
        reqSpec.set(buildHygraphRequestSpec().build());
    }

    protected static RequestSpecBuilder buildHygraphRequestSpec() {
        return buildRequestSpec().setBaseUri(HYGRAPH_VIDEO_STREAMING_URI);
    }
}
