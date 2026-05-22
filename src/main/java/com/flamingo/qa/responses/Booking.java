package com.flamingo.qa.responses;

import com.flamingo.qa.enums.AdditionalNeeds;
import com.flamingo.qa.requests.BookingDates;
import lombok.Data;

@Data
public class Booking {
    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private AdditionalNeeds additionalneeds;
}
