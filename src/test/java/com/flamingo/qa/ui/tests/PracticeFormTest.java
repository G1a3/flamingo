package com.flamingo.qa.ui.tests;

import com.flamingo.qa.ui.model.PracticeFormData;
import com.flamingo.qa.ui.pages.PracticeFormPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

@Feature("UI — Practice Form")
class PracticeFormTest extends BaseUiTest {

    private final Faker faker = new Faker();

    @Test
    @Description("Submit DemoQA practice form with faker data and verify confirmation modal")
    void userCanSubmitPracticeForm() {
        // Bad practice
        PracticeFormData data = Allure.step(
                "Generate practice form test data",
                () -> PracticeFormData.fromFaker(faker));

        new PracticeFormPage(page).open()
                .fillForm(data)
                .submit()
                .verifyFormIsSubmitted(data);
    }

    @Test
    @Description("Submit empty practice form and verify confirmation modal is not displayed")
    void userCannotSubmitPracticeFormWithoutMandatoryFields() {
        new PracticeFormPage(page).open()
                .submit()
                .verifyFormIsNotSubmitted();
    }
}
