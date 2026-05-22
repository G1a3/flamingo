package com.flamingo.qa.support;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllureListener implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Object testInstance = context.getRequiredTestInstance();
        if (!(testInstance instanceof PlaywrightAware playwrightAware)) {
            return;
        }

        String screenshotName = context.getExecutionException().isPresent()
                ? "Final state (failed)"
                : "Final state (passed)";

        try {
            AllureAttachments.attachScreenshot(playwrightAware.getPage(), screenshotName);
            AllureAttachments.attachPageUrl(playwrightAware.getPage());
        } catch (RuntimeException ignored) {
            // Browser may already be unavailable if teardown started early
        }
    }
}
