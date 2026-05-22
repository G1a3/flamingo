package com.flamingo.qa.ui.tests;

import com.flamingo.qa.support.AllureListener;
import com.flamingo.qa.support.PlaywrightAware;
import com.flamingo.qa.ui.config.UiConfig;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("ui")
@ExtendWith(AllureListener.class)
public abstract class BaseUiTest implements PlaywrightAware {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @Override
    public Page getPage() {
        return page;
    }

    @BeforeEach
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(UiConfig.headless()));
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeBrowser() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
