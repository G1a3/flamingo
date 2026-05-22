package com.flamingo.qa.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookingIdsResponse {
    @JsonProperty("bookingid")
    private int bookingId;
}
