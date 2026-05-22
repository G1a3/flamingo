package com.flamingo.qa.support;

import com.flamingo.qa.di.TestModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public final class TestInjector {

    private static final Injector INJECTOR = Guice.createInjector(new TestModule());

    private TestInjector() {
    }

    public static void injectMembers(Object target) {
        INJECTOR.injectMembers(target);
    }
}
