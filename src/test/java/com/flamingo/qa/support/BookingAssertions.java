package com.flamingo.qa.support;

import com.flamingo.qa.requests.BookingRequest;
import com.flamingo.qa.responses.Booking;
import com.flamingo.qa.responses.BookingResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public final class BookingAssertions {

    private static final String STATUS_CODE_MESSAGE = "Incorrect response status code returned";

    private BookingAssertions() {
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        assertThat(response.getStatusCode())
                .as(STATUS_CODE_MESSAGE)
                .isEqualTo(expectedStatusCode);
    }

    public static void assertOk(Response response) {
        assertStatusCode(response, HttpStatus.SC_OK);
    }

    public static void assertCreatedBookingMatchesRequest(BookingResponse bookingResponse,
                                                            BookingRequest bookingRequest) {
        assertThat(bookingResponse.getBookingid()).as("bookingid").isNotNull().isPositive();
        assertBookingMatchesRequest(bookingResponse.getBooking(), bookingRequest);
    }

    public static void assertBookingMatchesRequest(Booking booking, BookingRequest bookingRequest) {
        var requestDates = bookingRequest.getBookingdates();
        var responseDates = booking.getBookingdates();

        assertThat(booking.getFirstname()).as("firstname").isEqualTo(bookingRequest.getFirstname());
        assertThat(booking.getLastname()).as("lastname").isEqualTo(bookingRequest.getLastname());
        assertThat(booking.getTotalprice()).as("totalprice").isEqualTo(bookingRequest.getTotalprice());
        assertThat(booking.getDepositpaid()).as("depositpaid").isEqualTo(bookingRequest.getDepositpaid());
        assertThat(responseDates.getCheckin()).as("checkin").isEqualTo(requestDates.getCheckin());
        assertThat(responseDates.getCheckout()).as("checkout").isEqualTo(requestDates.getCheckout());
        assertThat(booking.getAdditionalneeds()).as("additionalneeds")
                .isEqualTo(bookingRequest.getAdditionalneeds());
    }
}
