package com.flamingo.qa.graphql.hygraph;

import lombok.Data;

@Data
public class Movie {
    private String id;
    private String title;
    private User publishedBy;
}
