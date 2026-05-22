package com.flamingo.qa.api.graphql;

import com.flamingo.qa.controller.GraphqlController;
import com.flamingo.qa.support.GraphqlAssertions;
import com.google.inject.Inject;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Hygraph GraphQL — negative")
class HygraphNegativeGraphqlTest extends BaseGraphqlTest {

    @Inject
    private GraphqlController graphqlController;

    @Test
    @Description("Non-existent movie ID returns HTTP 200 with data.movie = null and no errors")
    void queryMovieWithNonExistentIdReturnsNullDataAndHttp200() {
        Response response = graphqlController.post(HygraphQueries.MOVIE_BY_INVALID_ID);

        GraphqlAssertions.assertStatusCode(response, HttpStatus.SC_OK);
        assertThat(response.jsonPath().<Object>get("data.movie")).isNull();
        assertThat(response.jsonPath().getList("errors")).isNullOrEmpty();
    }

    @Test
    @Description("Malformed query returns errors[].message and no data")
    void malformedQueryReturnsErrorsAndNoData() {
        Response response = graphqlController.post(HygraphQueries.MALFORMED_QUERY);

        GraphqlAssertions.assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        assertThat(response.jsonPath().<Object>get("data")).isNull();
        assertThat(response.jsonPath().getString("errors[0].message"))
                .containsIgnoringCase("parse");
    }

    @Test
    @Description("Non-existent field returns validation error in errors[].message")
    void queryNonExistentFieldReturnsValidationError() {
        Response response = graphqlController.post(HygraphQueries.MOVIE_WITH_UNKNOWN_FIELD);

        GraphqlAssertions.assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        assertThat(response.jsonPath().<Object>get("data")).isNull();
        assertThat(response.jsonPath().getString("errors[0].message"))
                .containsIgnoringCase("nonExistentField")
                .containsIgnoringCase("not defined");
    }
}
