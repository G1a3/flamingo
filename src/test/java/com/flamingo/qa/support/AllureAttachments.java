package com.flamingo.qa.support;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;


public final class AllureAttachments {

    private AllureAttachments() {
    }

    public static void attachScreenshot(Page page, String name) {
        if (page == null) {
            return;
        }
        byte[] screenshot = page.screenshot();
        Allure.getLifecycle().addAttachment(name, "image/png", "png", screenshot);
    }

    public static void attachPageUrl(Page page) {
        if (page == null) {
            return;
        }
        Allure.addAttachment("Page URL", page.url());
    }
}
