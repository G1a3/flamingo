package com.flamingo.qa.requests;

import com.flamingo.qa.enums.AdditionalNeeds;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingRequest {
    private String firstname;
    private String lastname;
    private Number totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private AdditionalNeeds additionalneeds;
}
