package com.flamingo.qa.graphql;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class GraphqlRequest {
    private String query;
    @Builder.Default
    private Map<String, Object> variables = Map.of();
}
