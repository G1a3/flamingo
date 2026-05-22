package com.flamingo.qa.api.booking;

import com.flamingo.qa.api.BaseBookerApiTest;
import com.flamingo.qa.enums.AdditionalNeeds;
import com.flamingo.qa.requests.BookingRequest;
import com.flamingo.qa.responses.BookingResponse;
import com.flamingo.qa.support.BookingAssertions;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

@Feature("Delete Booking API Tests")
class DeleteBookingTest extends BaseBookerApiTest {

    @Test
    @Description("DELETE /booking/{id}: 201 when deleting booking by id")
    void userIsAbleToDeleteExistingBooking() {
        BookingRequest bookingRequest = bookingData.randomBooking(AdditionalNeeds.DINNER);

        Response createResponse = bookingController.bookingPOST(bookingRequest);
        BookingAssertions.assertOk(createResponse);
        int bookingId = createResponse.as(BookingResponse.class).getBookingid();

        Response deleteResponse = bookingController.bookingDELETE(bookingId);
        BookingAssertions.assertStatusCode(deleteResponse, HttpStatus.SC_CREATED);

        Response getResponse = bookingController.bookingGET(bookingId);
        BookingAssertions.assertStatusCode(getResponse, HttpStatus.SC_NOT_FOUND);
    }
}
