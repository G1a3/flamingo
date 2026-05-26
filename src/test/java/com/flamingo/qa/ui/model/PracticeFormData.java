package com.flamingo.qa.ui.model;

import net.datafaker.Faker;

import java.time.format.TextStyle;
import java.util.Locale;

public record PracticeFormData(
        String firstName,
        String lastName,
        String email,
        String mobile,
        String birthMonth,
        String birthYear,
        String birthDayClass,
        String birthDateDisplay,
        String subject,
        String address,
        String state,
        String city
) {
    private static final String[] SUBJECTS = {
            "Maths", "Physics", "Chemistry", "Computer Science", "Commerce",
            "Economics", "Arts", "English", "Hindi", "Biology", "History", "Civics"
    };

    private static final StateCity[] LOCATIONS = {
            new StateCity("NCR", "Delhi", "Gurgaon", "Noida"),
            new StateCity("Uttar Pradesh", "Lucknow", "Merrut", "Kanpur"),
            new StateCity("Haryana", "Karnal", "Panipat"),
            new StateCity("Rajasthan", "Jaipur", "Jaiselmer", "Mount Abu")
    };

    public static PracticeFormData fromFaker(Faker faker) {
        var dob = faker.timeAndDate().birthday(18, 65);
        var month = dob.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        StateCity location = faker.options().option(LOCATIONS);

        return new PracticeFormData(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                faker.number().digits(10),
                month,
                String.valueOf(dob.getYear()),
                String.format("%03d", dob.getDayOfMonth()),
                dob.getDayOfMonth() + " " + month + "," + dob.getYear(),
                faker.options().option(SUBJECTS),
                faker.address().streetAddress(),
                location.state(),
                location.randomCity(faker)
        );
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public String stateAndCity() {
        return state + " " + city;
    }

    private record StateCity(String state, String... cities) {
        String randomCity(Faker faker) {
            return faker.options().option(cities);
        }
    }
}
