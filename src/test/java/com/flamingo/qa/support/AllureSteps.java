package com.flamingo.qa.support;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.util.function.Supplier;

public final class AllureSteps {

    private AllureSteps() {
    }

    public static void step(String name, Page page, Runnable action) {
        Allure.step(name, () -> {
            try {
                action.run();
            } finally {
                attachStepScreenshot(page, name);
            }
        });
    }

    public static <T> T step(String name, Page page, Supplier<T> action) {
        return Allure.step(name, () -> {
            try {
                return action.get();
            } finally {
                attachStepScreenshot(page, name);
            }
        });
    }

    private static void attachStepScreenshot(Page page, String stepName) {
        AllureAttachments.attachScreenshot(page, stepName);
    }
}
