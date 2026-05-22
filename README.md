# QA Automation Test Suite

Automated API and UI tests for the QA assignment, built with Java 17, JUnit 5, REST Assured, Playwright, AssertJ, and Allure.

## Prerequisites

- Java 17+ (project compiles with `--release 17`; Java 11+ compatible with toolchain adjustment)
- Maven 3.6+
- Chrome/Chromium (installed automatically by Playwright on first UI test run)

## Project Structure

```
src/main/java/com/flamingo/qa/
├── client/          # REST & GraphQL API clients
├── controller/      # Request orchestration (Booking, GraphQL)
├── requests/        # REST request models
├── responses/       # REST response models
├── graphql/         # GraphQL DTOs
└── enums/

src/test/java/com/flamingo/qa/
├── api/booking/     # Restful Booker CRUD tests
├── api/graphql/     # Hygraph GraphQL tests
├── ui/              # Page Object Model, UI tests, config
├── support/         # Assertions, Allure helpers, test utilities
└── di/              # Guice dependency injection for tests
```

## How to Run

```bash
# Run all tests
mvn clean test

# Run only API tests (REST Booker + GraphQL)
mvn test -Dgroups="api"

# Run only REST Booker tests
mvn test -Dgroups="api" -DexcludedGroups="graphql"

# Run only GraphQL tests
mvn test -Dgroups="graphql"

# Run only UI tests
mvn test -Dgroups="ui"
```

### Playwright setup (first run / CI)

```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

### Allure report

```bash
mvn clean test
mvn allure:serve
```

Use `mvn allure:serve` (local HTTP server). Opening `target/allure-report/index.html` directly in a browser often shows a blank "Loading..." page due to browser security restrictions.

### Configuration

| File | Purpose |
|------|---------|
| `src/test/resources/ui.properties` | UI base URL, headless mode |
| `src/test/resources/environment.properties` | Allure environment metadata |
| `src/test/resources/allure.properties` | Allure results directory |

Override at runtime, e.g. `-Dheadless=false` for headed UI debugging.

## Test Coverage

| Area | Tests | Description |
|------|-------|-------------|
| **REST API (Booker)** | 16 | CRUD: create, read, update, delete; positive & negative payloads |
| **GraphQL (Hygraph)** | 7 | Queries with variables, fragments, pagination; invalid ID, malformed query, unknown field |
| **UI (DemoQA)** | 2 | Happy path form submit; submit without mandatory fields |
| **Total** | **25** | |

### API — Restful Booker (`@Tag("api")`)

- **Create** — full payload, mandatory fields only, boundary dates/prices, missing required fields (500)
- **Read** — get booking by ID, list booking IDs, duplicate ID check
- **Update** — update existing booking
- **Delete** — delete and verify 404 on subsequent GET

### API — Hygraph GraphQL (`@Tag("api")` + `@Tag("graphql")`)

- **Positive** — list with limit, get by ID, variables, fragments with nested fields
- **Negative** — non-existent ID, malformed query, unknown field validation

### UI — Practice Form (`@Tag("ui")`)

- **Positive** — fill form with DataFaker data, submit, verify confirmation modal
- **Negative** — click Submit with empty mandatory fields, verify modal is not shown

## Test Strategy

The framework prioritizes **maintainability and clear separation of concerns** over raw test count.

1. **Layered architecture** — Tests call controllers, not raw HTTP. Clients handle auth, serialization, and Allure REST attachments. This keeps tests focused on behavior and assertions.

2. **Positive and negative coverage** — API tests cover happy paths (CRUD, valid GraphQL) and failure modes (validation errors, 404, 500 for invalid payloads). UI tests cover successful submission and blocked submission without required data.

3. **Reusable test data** — `BookingDataFactory` and DataFaker generate dynamic data to reduce flakiness from hard-coded values and support parallel-safe runs.

4. **Consistent assertions** — `BookingAssertions` and `GraphqlAssertions` centralize status-code and response checks; UI uses Playwright assertions with AssertJ-style readability.

5. **Reporting** — Allure captures REST request/response bodies, UI steps with per-step screenshots, and final page state on pass/fail. JUnit tags (`api`, `graphql`, `ui`) enable selective execution.

6. **Respect for public services** — Tests use realistic payloads and avoid aggressive load; external services may reset data (Restful Booker) or rate-limit — failures are investigated before retrying.

## Challenges & Solutions

| Challenge | Solution |
|-----------|----------|
| Restful Booker data resets | Tests create their own bookings via API setup; no dependency on persistent IDs |
| GraphQL error shape varies | Dedicated negative tests assert `errors[]` messages and null `data` |
| Allure report blank when opened as file | Documented `mvn allure:serve`; CI uploads report as artifact |
| UI screenshots after browser closed | `AllureListener` uses `AfterTestExecutionCallback` (runs before `@AfterEach` teardown) |
| Playwright browsers missing locally/CI | `playwright install` in README and GitHub Actions workflow |
| DemoQA form overlays / forced clicks | Playwright `force` click on gender, hobby, and submit where elements are obscured |

## What I Would Add With More Time

- **AspectJ `@Step` annotations** instead of programmatic `Allure.step()` for cleaner page objects
- **Parallel test execution** with JUnit 5 parallel config and isolated API test data
- **Data-driven tests** (`@ParameterizedTest`) for booking boundary values and GraphQL query variants
- **Custom waits** — centralized Playwright wait utilities for slow DemoQA widgets
- **API contract checks** — JSON Schema validation on REST and GraphQL responses
- **Enrollment / additional UI flows** if required by expanded scope
## CI/CD

GitHub Actions workflow **Regression suite** (`.github/workflows/regression-suite.yml`) runs on push/PR to `main`/`master`:

1. Java 17 + Maven cache
2. Playwright Chromium + OS dependencies install
3. `mvn test`
4. Allure report generation (pass or fail)
5. **Published Allure report** on GitHub Pages with a clickable link

### Allure report link (after each CI run)

1. Open **Actions** → **Regression suite** → latest run → **Summary**
2. Click **Allure Report** (or open `https://g1a3.github.io/flamingo/` after Pages is enabled)

The workflow deploys `target/allure-report` to the `gh-pages` branch. After a successful run on `main`, open:

**https://g1a3.github.io/flamingo/**

(Allow 1–2 minutes after the first deploy for GitHub to publish the site.)

If you still see a 404: [Settings → Pages](https://github.com/G1a3/flamingo/settings/pages) → **Deploy from a branch** → `gh-pages` / `(root)` → Save, then re-run the workflow.

Download the **allure-report** artifact as a fallback.

## Tech Stack

- Java 17, Maven
- JUnit 5, AssertJ, DataFaker
- REST Assured, Jackson
- Playwright
- Google Guice (test DI)
- Allure Report 2.x
