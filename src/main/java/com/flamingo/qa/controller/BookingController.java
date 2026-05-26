package com.flamingo.qa.controller;

import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.requests.BookingRequest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class BookingController extends BookerApiClient {

    private static final String BOOKING_PATH = "/booking";
    private static final String NO_CACHE = "no-cache";

    public Response bookingPOST(BookingRequest bookingRequest) {
        return given().spec(requestSpec())
                .basePath(BOOKING_PATH)
                .body(bookingRequest)
                .post()
                .then()
                .extract()
                .response();
    }

    // Bad practice to check status code here
    public Response bookingPUT(int bookingId, BookingRequest bookingRequest) {
        return given().spec(authenticatedRequestSpec())
                .basePath(bookingPath(bookingId))
                .body(bookingRequest)
                .put()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
    }

    public Response bookingDELETE(int bookingId) {
        return given().spec(authenticatedRequestSpec())
                .basePath(bookingPath(bookingId))
                .delete()
                .then()
                .extract()
                .response();
    }

    public Response bookingIdsGET() {
        return given().spec(requestSpec())
                .header("Cache-Control", NO_CACHE)
                .header("Pragma", NO_CACHE)
                .basePath(BOOKING_PATH)
                .get()
                .then()
                .extract()
                .response();
    }

    public Response bookingGET(int bookingId) {
        return given().spec(requestSpec())
                .header("Cache-Control", NO_CACHE)
                .header("Pragma", NO_CACHE)
                .basePath(bookingPath(bookingId))
                .get()
                .then()
                .extract()
                .response();
    }

    private static String bookingPath(int bookingId) {
        return BOOKING_PATH + "/" + bookingId;
    }
}
