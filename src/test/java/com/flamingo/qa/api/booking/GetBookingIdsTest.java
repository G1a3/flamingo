package com.flamingo.qa.api.booking;

import com.flamingo.qa.api.BaseBookerApiTest;
import com.flamingo.qa.enums.AdditionalNeeds;
import com.flamingo.qa.requests.BookingRequest;
import com.flamingo.qa.responses.BookingIdsResponse;
import com.flamingo.qa.responses.BookingResponse;
import com.flamingo.qa.support.BookingAssertions;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Feature("Get Booking Id's API Tests")
class GetBookingIdsTest extends BaseBookerApiTest {

    @Test
    @Description("GET /booking: 200 when getting existing booking id's")
    void verifyCreatedBookingIdIsInListOfBookingIds() {
        BookingRequest bookingRequest = bookingData.randomBooking(AdditionalNeeds.DINNER);

        Response createResponse = bookingController.bookingPOST(bookingRequest);
        BookingAssertions.assertOk(createResponse);
        int bookingId = createResponse.as(BookingResponse.class).getBookingid();

        await()
                .atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    Response response = bookingController.bookingIdsGET();
                    BookingAssertions.assertOk(response);

                    List<BookingIdsResponse> bookingIds = response.as(new TypeRef<>() {});
                    assertThat(bookingIds.stream().map(BookingIdsResponse::getBookingId))
                            .as("GET /booking should include created booking id: '%s'", bookingId)
                            .contains(bookingId);
                });
    }

    @Test
    @Description("GET /booking: verify booking id list doesn't have duplicates")
    void verifyBookingIdListDoesNotContainDuplicateValues() {
        Response response = bookingController.bookingIdsGET();
        List<Integer> bookingIds = response.as(new TypeRef<List<BookingIdsResponse>>() {})
                .stream()
                .map(BookingIdsResponse::getBookingId)
                .toList();
        assertThat(bookingIds).as("Booking id's list contains duplicate values").doesNotHaveDuplicates();
    }
}
