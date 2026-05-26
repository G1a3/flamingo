package com.flamingo.qa.ui.pages;

import com.flamingo.qa.support.AllureSteps;
import com.flamingo.qa.ui.config.UiConfig;
import com.flamingo.qa.ui.model.PracticeFormData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PracticeFormPage extends BasePage {

    private static final String SUBMISSION_TITLE = "Thanks for submitting the form";
    private static final String GENDER_MALE = "Male";
    private static final String HOBBY_SPORTS = "Sports";

    private final Locator firstName = page.locator("#firstName");
    private final Locator lastName = page.locator("#lastName");
    private final Locator email = page.locator("#userEmail");
    private final Locator mobile = page.locator("#userNumber");
    private final Locator dateOfBirth = page.locator("#dateOfBirthInput");
    private final Locator subjects = page.locator("#subjectsInput");
    private final Locator address = page.locator("#currentAddress");
    private final Locator stateDropdown = page.locator("#state");
    private final Locator cityDropdown = page.locator("#city");
    private final Locator submitButton = page.locator("#submit");
    private final Locator modal = page.locator(".modal-content");
    private final Locator modalTitle = page.locator("#example-modal-sizes-title-lg");
    private final Locator modalTable = page.locator(".table");

    public PracticeFormPage(Page page) {
        super(page);
    }

    public PracticeFormPage open() {
        AllureSteps.step("Open practice form", page, () -> page.navigate(UiConfig.baseUrl()));
        return this;
    }

    public PracticeFormPage fillForm(PracticeFormData data) {
        AllureSteps.step("Fill practice form", page, () -> enterFirstName(data.firstName())
                .enterLastName(data.lastName())
                .enterEmail(data.email())
                .selectGenderMale()
                .enterMobile(data.mobile())
                .selectDateOfBirth(data.birthMonth(), data.birthYear(), data.birthDayClass())
                .selectHobbySports()
                .addSubject(data.subject())
                .enterAddress(data.address())
                .selectStateAndCity(data.state(), data.city()));
        return this;
    }

    public PracticeFormPage enterFirstName(String value) {
        AllureSteps.step("Enter first name: " + value, page, () -> firstName.fill(value));
        return this;
    }

    public PracticeFormPage enterLastName(String value) {
        AllureSteps.step("Enter last name: " + value, page, () -> lastName.fill(value));
        return this;
    }

    public PracticeFormPage enterEmail(String value) {
        AllureSteps.step("Enter email: " + value, page, () -> email.fill(value));
        return this;
    }

    public PracticeFormPage selectGenderMale() {
        AllureSteps.step("Select gender: Male", page,
                () -> page.locator("#gender-radio-1").click(new Locator.ClickOptions().setForce(true)));
        return this;
    }

    public PracticeFormPage enterMobile(String value) {
        AllureSteps.step("Enter mobile: " + value, page, () -> mobile.fill(value));
        return this;
    }

    public PracticeFormPage selectDateOfBirth(String month, String year, String dayClass) {
        AllureSteps.step("Select date of birth", page, () -> {
            dateOfBirth.click();
            page.locator(".react-datepicker__month-select").selectOption(month);
            page.locator(".react-datepicker__year-select").selectOption(year);
            page.locator(".react-datepicker__day--" + dayClass + ":not(.react-datepicker__day--outside-month)")
                    .click();
        });
        return this;
    }

    public PracticeFormPage addSubject(String subject) {
        AllureSteps.step("Add subject: " + subject, page, () -> {
            subjects.fill(subject);
            page.getByText(subject, new Page.GetByTextOptions().setExact(true)).click();
        });
        return this;
    }

    public PracticeFormPage selectHobbySports() {
        AllureSteps.step("Select hobby: Sports", page,
                () -> page.locator("label[for='hobbies-checkbox-1']")
                        .click(new Locator.ClickOptions().setForce(true)));
        return this;
    }

    public PracticeFormPage enterAddress(String value) {
        AllureSteps.step("Enter address", page, () -> address.fill(value));
        return this;
    }

    public PracticeFormPage selectStateAndCity(String state, String city) {
        AllureSteps.step("Select state and city: " + state + ", " + city, page, () -> {
            stateDropdown.click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(state).setExact(true))
                    .first()
                    .click();
            cityDropdown.click();
            Locator cityOption = page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(city).setExact(true))
                    .last();
            cityOption.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            cityOption.scrollIntoViewIfNeeded();
            cityOption.click();
        });
        return this;
    }

    public PracticeFormPage submit() {
        AllureSteps.step("Submit practice form", page,
                () -> submitButton.click(new Locator.ClickOptions().setForce(true)));
        return this;
    }

    public PracticeFormPage verifyFormIsNotSubmitted() {
        AllureSteps.step("Verify submission modal is not shown", page, () -> {
            assertThat(modal).not().isVisible();
            assertThat(firstName).isVisible();
            assertThat(submitButton).isVisible();
        });
        return this;
    }

    public PracticeFormPage verifyFormIsSubmitted(PracticeFormData data) {
        AllureSteps.step("Verify submission confirmation modal", page, () -> {
            assertThat(modal).isVisible();
            assertThat(modalTitle).hasText(SUBMISSION_TITLE);
            assertThat(modalTable).containsText("Student Name");
            assertThat(modalTable).containsText(data.fullName());
            assertThat(modalTable).containsText(data.email());
            assertThat(modalTable).containsText(GENDER_MALE);
            assertThat(modalTable).containsText(data.mobile());
            assertThat(modalTable).containsText(data.birthDateDisplay());
            assertThat(modalTable).containsText(data.subject());
            assertThat(modalTable).containsText(HOBBY_SPORTS);
            assertThat(modalTable).containsText(data.address());
            assertThat(modalTable).containsText(data.stateAndCity());
        });
        return this;
    }
}
