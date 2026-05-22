package com.flamingo.qa.graphql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphqlResponse<T> {
    private T data;
    private List<GraphqlError> errors;
}
