package com.flamingo.qa.api.graphql;

import com.flamingo.qa.controller.GraphqlController;
import com.flamingo.qa.graphql.GraphqlResponse;
import com.flamingo.qa.graphql.hygraph.MovieData;
import com.flamingo.qa.graphql.hygraph.MoviesData;
import com.flamingo.qa.support.GraphqlAssertions;
import com.google.inject.Inject;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Hygraph GraphQL — positive")
class HygraphPositiveGraphqlTest extends BaseGraphqlTest {

    private static final int MOVIES_LIMIT = 3;

    @Inject
    private GraphqlController graphqlController;

    @Test
    @Description("Query movies list with pagination limit (first)")
    void queryMoviesListWithPaginationLimit() {
        Response response = graphqlController.post(
                HygraphQueries.MOVIES_WITH_LIMIT,
                Map.of("first", MOVIES_LIMIT));
        GraphqlAssertions.assertStatusCode(response, HttpStatus.SC_OK);

        GraphqlResponse<MoviesData> body = response.as(new TypeRef<>() {});
        GraphqlAssertions.assertOkWithoutErrors(body);
        assertThat(body.getData().getMovies())
                .hasSize(MOVIES_LIMIT)
                .allSatisfy(movie -> {
                    assertThat(movie.getId()).isNotBlank();
                    assertThat(movie.getTitle()).isNotBlank();
                });
    }

    @Test
    @Description("Query a single movie by ID")
    void querySingleMovieById() {
        String movieId = fetchFirstMovieId();

        Response response = graphqlController.post(
                HygraphQueries.MOVIE_BY_ID,
                Map.of("id", movieId));
        GraphqlAssertions.assertStatusCode(response, HttpStatus.SC_OK);

        GraphqlResponse<MovieData> body = response.as(new TypeRef<>() {});
        GraphqlAssertions.assertOkWithoutErrors(body);
        assertThat(body.getData().getMovie())
                .isNotNull()
                .satisfies(movie -> {
                    assertThat(movie.getId()).isEqualTo(movieId);
                    assertThat(movie.getTitle()).isNotBlank();
                });
    }

    @Test
    @Description("Query movie by ID using GraphQL variables (not string interpolation)")
    void queryMovieByIdWithGraphqlVariables() {
        String movieId = fetchFirstMovieId();

        Response response = graphqlController.post(
                HygraphQueries.MOVIE_BY_ID,
                Map.of("id", movieId));

        GraphqlAssertions.assertStatusCode(response, HttpStatus.SC_OK);
        assertThat(response.jsonPath().getString("data.movie.id")).isEqualTo(movieId);
        assertThat(response.jsonPath().getList("errors")).isNull();
    }

    @Test
    @Description("Query with fragment and nested fields: movie → publishedBy → name")
    void queryMoviesWithFragmentAndNestedPublishedBy() {
        Response response = graphqlController.post(
                HygraphQueries.MOVIES_WITH_PUBLISHER_FRAGMENT,
                Map.of("first", MOVIES_LIMIT));
        GraphqlAssertions.assertStatusCode(response, HttpStatus.SC_OK);

        GraphqlResponse<MoviesData> body = response.as(new TypeRef<>() {});
        GraphqlAssertions.assertOkWithoutErrors(body);
        assertThat(body.getData().getMovies())
                .hasSize(MOVIES_LIMIT)
                .allSatisfy(movie -> {
                    assertThat(movie.getTitle()).isNotBlank();
                    assertThat(movie.getPublishedBy()).isNotNull();
                    assertThat(movie.getPublishedBy().getName()).isNotBlank();
                });
    }

    private String fetchFirstMovieId() {
        Response response = graphqlController.post(
                HygraphQueries.MOVIES_WITH_LIMIT,
                Map.of("first", 1));
        return response.jsonPath().getString("data.movies[0].id");
    }
}
