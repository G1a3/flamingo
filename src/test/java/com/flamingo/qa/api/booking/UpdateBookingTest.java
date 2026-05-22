package com.flamingo.qa.api.booking;

import com.flamingo.qa.api.BaseBookerApiTest;
import com.flamingo.qa.enums.AdditionalNeeds;
import com.flamingo.qa.requests.BookingRequest;
import com.flamingo.qa.responses.Booking;
import com.flamingo.qa.responses.BookingResponse;
import com.flamingo.qa.support.BookingAssertions;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Update Booking API Tests")
class UpdateBookingTest extends BaseBookerApiTest {

    @Test
    @Description("PUT /booking/{id}: 200 when updating booking by id")
    void userIsAbleToUpdateExistingBooking() {
        BookingRequest originalRequest = bookingData.randomBooking(AdditionalNeeds.WIFI);

        Response createResponse = bookingController.bookingPOST(originalRequest);
        BookingAssertions.assertOk(createResponse);

        BookingResponse created = createResponse.as(BookingResponse.class);
        int bookingId = created.getBookingid();
        Booking originalBooking = created.getBooking();

        LocalDate newCheckin = originalBooking.getBookingdates().getCheckin().plusDays(30);
        LocalDate newCheckout = originalBooking.getBookingdates().getCheckout().plusDays(35);

        BookingRequest updateRequest = bookingData.buildBooking(builder -> builder
                .depositpaid(!originalRequest.getDepositpaid())
                .additionalneeds(AdditionalNeeds.BREAKFAST)
                .bookingdates(bookingData.bookingDates(newCheckin, newCheckout)));

        Response updateResponse = bookingController.bookingPUT(bookingId, updateRequest);
        BookingAssertions.assertOk(updateResponse);

        Booking updatedBooking = updateResponse.as(Booking.class);
        BookingAssertions.assertBookingMatchesRequest(updatedBooking, updateRequest);

        assertThat(updatedBooking.getFirstname()).as("firstname").isNotEqualTo(originalBooking.getFirstname());
        assertThat(updatedBooking.getLastname()).as("lastname").isNotEqualTo(originalBooking.getLastname());
        assertThat(updatedBooking.getTotalprice()).as("totalprice").isNotEqualTo(originalBooking.getTotalprice());
        assertThat(updatedBooking.getDepositpaid()).as("depositpaid").isNotEqualTo(originalBooking.getDepositpaid());
        assertThat(updatedBooking.getBookingdates().getCheckin()).as("checkin")
                .isEqualTo(updateRequest.getBookingdates().getCheckin())
                .isNotEqualTo(originalBooking.getBookingdates().getCheckin());
        assertThat(updatedBooking.getBookingdates().getCheckout()).as("checkout")
                .isEqualTo(updateRequest.getBookingdates().getCheckout())
                .isNotEqualTo(originalBooking.getBookingdates().getCheckout());
        assertThat(updatedBooking.getAdditionalneeds()).as("additionalneeds")
                .isNotEqualTo(originalBooking.getAdditionalneeds());
    }
}
