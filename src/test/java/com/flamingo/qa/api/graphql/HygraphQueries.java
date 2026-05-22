package com.flamingo.qa.api.graphql;

final class HygraphQueries {

    static final String MOVIES_WITH_LIMIT = """
            query MoviesWithLimit($first: Int!) {
              movies(first: $first) {
                id
                title
              }
            }
            """;

    static final String MOVIE_BY_ID = """
            query MovieById($id: ID!) {
              movie(where: { id: $id }) {
                id
                title
              }
            }
            """;

    static final String MOVIES_WITH_PUBLISHER_FRAGMENT = """
            fragment PublisherName on User {
              name
            }

            query MoviesWithPublisher($first: Int!) {
              movies(first: $first) {
                title
                publishedBy {
                  ...PublisherName
                }
              }
            }
            """;

    static final String MOVIE_BY_INVALID_ID = """
            query MovieByInvalidId {
              movie(where: { id: "000000000000000000000000" }) {
                id
                title
              }
            }
            """;

    static final String MALFORMED_QUERY = "{ movies { title ";

    static final String MOVIE_WITH_UNKNOWN_FIELD = """
            query MovieWithUnknownField {
              movies(first: 1) {
                nonExistentField
              }
            }
            """;

    private HygraphQueries() {
    }
}
