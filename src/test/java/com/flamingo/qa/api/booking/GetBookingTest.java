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

@Feature("Get Booking API Tests")
class GetBookingTest extends BaseBookerApiTest {

    @Test
    @Description("GET /booking/{id}: 200 when getting booking by id")
    void getBookingById() {
        BookingRequest bookingRequest = bookingData.randomBooking(AdditionalNeeds.LUNCH);

        Response createResponse = bookingController.bookingPOST(bookingRequest);
        BookingAssertions.assertOk(createResponse);
        int bookingId = createResponse.as(BookingResponse.class).getBookingid();

        Response getResponse = bookingController.bookingGET(bookingId);
        BookingAssertions.assertOk(getResponse);

        BookingAssertions.assertBookingMatchesRequest(getResponse.as(Booking.class), bookingRequest);
    }
}
