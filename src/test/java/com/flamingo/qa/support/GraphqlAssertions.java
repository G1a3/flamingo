package com.flamingo.qa.support;

import com.flamingo.qa.graphql.GraphqlResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public final class GraphqlAssertions {

    private GraphqlAssertions() {
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
    }

    public static <T> void assertOkWithoutErrors(GraphqlResponse<T> body) {
        assertThat(body.getErrors()).isNullOrEmpty();
        assertThat(body.getData()).isNotNull();
    }
}
