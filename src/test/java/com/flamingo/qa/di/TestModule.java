package com.flamingo.qa.di;

import com.flamingo.qa.controller.BookingController;
import com.flamingo.qa.controller.GraphqlController;
import com.flamingo.qa.support.BookingDataFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.datafaker.Faker;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BookingController.class).in(Singleton.class);
        bind(GraphqlController.class).in(Singleton.class);
        bind(BookingDataFactory.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    Faker faker() {
        return new Faker();
    }
}
