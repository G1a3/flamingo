package com.flamingo.qa.support;

import com.flamingo.qa.enums.AdditionalNeeds;
import com.flamingo.qa.requests.BookingDates;
import com.flamingo.qa.requests.BookingRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.function.Consumer;

@Singleton
public class BookingDataFactory {

    private final Faker faker;

    @Inject
    public BookingDataFactory(Faker faker) {
        this.faker = faker;
    }

    public BookingRequest randomBooking(AdditionalNeeds additionalNeeds) {
        return randomBookingBuilder()
                .additionalneeds(additionalNeeds)
                .build();
    }

    public BookingRequest randomBookingWithoutAdditionalNeeds() {
        return randomBookingBuilder().build();
    }

    public BookingRequest randomBookingWithTotalPrice(int totalPrice) {
        return randomBookingBuilder()
                .totalprice(totalPrice)
                .build();
    }

    public BookingRequest randomBookingWithDates(LocalDate checkin, LocalDate checkout) {
        return randomBookingBuilder()
                .bookingdates(bookingDates(checkin, checkout))
                .build();
    }

    public BookingRequest buildBooking(Consumer<BookingRequest.BookingRequestBuilder> customizer) {
        var builder = randomBookingBuilder();
        customizer.accept(builder);
        return builder.build();
    }

    public BookingRequest.BookingRequestBuilder randomBookingBuilder() {
        var checkin = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        var checkout = checkin.plusDays(faker.number().numberBetween(1, 14));
        return BookingRequest.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(50, 999))
                .depositpaid(faker.bool().bool())
                .bookingdates(bookingDates(checkin, checkout));
    }

    public BookingDates randomBookingDates() {
        var checkin = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        return bookingDates(checkin, checkin.plusDays(faker.number().numberBetween(1, 14)));
    }

    public BookingDates bookingDates(LocalDate checkin, LocalDate checkout) {
        return BookingDates.builder()
                .checkin(checkin)
                .checkout(checkout)
                .build();
    }
}
