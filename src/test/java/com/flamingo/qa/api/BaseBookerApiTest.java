package com.flamingo.qa.api;

import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.controller.BookingController;
import com.flamingo.qa.support.BookingDataFactory;
import com.flamingo.qa.support.TestInjector;
import com.google.inject.Inject;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import com.flamingo.qa.support.AllureListener;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("api")
@ExtendWith(AllureListener.class)
public abstract class BaseBookerApiTest extends BookerApiClient {

    @Inject
    protected BookingController bookingController;
    @Inject
    protected Faker faker;
    @Inject
    protected BookingDataFactory bookingData;

    @BeforeEach
    void injectDependencies() {
        TestInjector.injectMembers(this);
    }
}
