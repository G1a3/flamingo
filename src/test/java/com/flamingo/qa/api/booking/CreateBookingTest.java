package com.flamingo.qa.api.booking;

import com.flamingo.qa.api.BaseBookerApiTest;
import com.flamingo.qa.enums.AdditionalNeeds;
import com.flamingo.qa.requests.BookingDates;
import com.flamingo.qa.requests.BookingRequest;
import com.flamingo.qa.responses.BookingResponse;
import com.flamingo.qa.support.BookingAssertions;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@Feature("Create Booking API Tests")
class CreateBookingTest extends BaseBookerApiTest {

    @Test
    @Description("POST /booking: 200 when creating booking with full payload")
    void createBookingWithFullPayload() {
        BookingRequest bookingRequest = bookingData.randomBooking(AdditionalNeeds.BREAKFAST);
        assertCreatedBooking(bookingRequest);
    }

    @Test
    @Description("POST /booking: 200 when creating booking with mandatory request parameters")
    void createBookingWithMandatoryRequestParameters() {
        BookingRequest bookingRequest = bookingData.randomBookingWithoutAdditionalNeeds();
        assertCreatedBooking(bookingRequest);
    }

    @Test
    @Description("POST /booking: 200 when creating booking with zero prise")
    void createBookingWithZeroTotalPrice() {
        BookingRequest bookingRequest = bookingData.randomBookingWithTotalPrice(0);
        assertCreatedBooking(bookingRequest);
    }

    @Test
    @Description("POST /booking: 200 when creating booking with checkout value in past")
    void createBookingWithCheckoutValueInPast() {
        var checkin = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        var checkout = checkin.minusDays(faker.number().numberBetween(1, 14));
        BookingRequest bookingRequest = bookingData.randomBookingWithDates(checkin, checkout);
        assertCreatedBooking(bookingRequest);
    }

    @Test
    @Description("POST /booking: 200 when creating booking with checkin value in past")
    void createBookingWithCheckInValueInPast() {
        var checkin = LocalDate.now().minusDays(faker.number().numberBetween(1, 30));
        var checkout = checkin.plusDays(faker.number().numberBetween(1, 14));
        BookingRequest bookingRequest = bookingData.randomBookingWithDates(checkin, checkout);
        assertCreatedBooking(bookingRequest);
    }

    @Test
    @Description("POST /booking: 500 when creating booking without 'firstName' mandatory request parameter")
    void internalServerErrorWhenCreatingBookingWithoutFirstNameMandatoryField() {
        BookingRequest bookingRequest = bookingData.buildBooking(builder -> builder.firstname(null));
        assertInternalServerError(bookingRequest);
    }

    @Test
    @Description("POST /booking: 500 when creating booking without 'lastName' mandatory request parameter")
    void internalServerErrorWhenCreatingBookingWithoutLastNameMandatoryField() {
        BookingRequest bookingRequest = bookingData.buildBooking(builder -> builder.lastname(null));
        assertInternalServerError(bookingRequest);
    }

    @Test
    @Description("POST /booking: 500 when creating booking without 'checkin' mandatory request parameter")
    void internalServerErrorWhenCreatingBookingWithoutCheckInMandatoryField() {
        BookingRequest bookingRequest = bookingData.buildBooking(builder ->
                builder.bookingdates(BookingDates.builder()
                        .checkout(bookingData.randomBookingDates().getCheckout())
                        .build()));
        assertInternalServerError(bookingRequest);
    }

    @Test
    @Description("POST /booking: 500 when creating booking without 'checkout' mandatory request parameter")
    void internalServerErrorWhenCreatingBookingWithoutCheckOutMandatoryField() {
        BookingRequest bookingRequest = bookingData.buildBooking(builder ->
                builder.bookingdates(BookingDates.builder()
                        .checkin(bookingData.randomBookingDates().getCheckin())
                        .build()));
        assertInternalServerError(bookingRequest);
    }

    @Test
    @Description("POST /booking: 500 when creating booking without 'totalPrice' mandatory request parameter")
    void internalServerErrorWhenCreatingBookingWithoutTotalPriceMandatoryField() {
        BookingRequest bookingRequest = bookingData.buildBooking(builder -> builder.totalprice(null));
        assertInternalServerError(bookingRequest);
    }

    @Test
    @Description("POST /booking: 500 when creating booking without 'depositPaid' mandatory request parameter")
    void internalServerErrorWhenCreatingBookingWithoutDepositPaidMandatoryField() {
        BookingRequest bookingRequest = bookingData.buildBooking(builder -> builder.depositpaid(null));
        assertInternalServerError(bookingRequest);
    }

    private void assertCreatedBooking(BookingRequest bookingRequest) {
        Response response = bookingController.bookingPOST(bookingRequest);
        BookingAssertions.assertOk(response);
        BookingAssertions.assertCreatedBookingMatchesRequest(response.as(BookingResponse.class), bookingRequest);
    }

    private void assertInternalServerError(BookingRequest bookingRequest) {
        Response response = bookingController.bookingPOST(bookingRequest);
        BookingAssertions.assertStatusCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
