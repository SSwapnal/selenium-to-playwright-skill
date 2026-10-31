# Conversion report

Prompt, run from the repo root in Claude Code:

```text
Convert the Selenium tests in this repo to Playwright.
```

The agent loaded the `selenium-to-playwright` skill on its own because the request matched the skill's description. It took 22 steps and about 70 seconds.

## The skill got better after one review

The first run passed all 8 tests, but a review found two style problems:

- Page objects had methods that only returned a locator, such as `heading()` next to a `headingLocator` field.
- One spec shared a `let nominations` variable between tests.

Two rules were added to `SKILL.md`: getters become `readonly` locator properties, and tests share no `let` variables. The conversion was then run again from scratch, and the code in `playwright/` comes from that second run.

The skill is a living document: each review adds a rule, so the next batch comes out better.

## Agent's report (second run)

I converted both Selenium test classes to Playwright, and all 8 tests pass.

**Files created** (in `playwright/`):
- `pages/LoginPage.ts`: `login()` stays an async method. `username`, `password`, `signInButton` and `error` are `readonly` locators.
- `pages/NominationsPage.ts`: `submit()` and `logout()` stay async methods. `heading`, `welcome`, `toast`, `error`, `rows` and `count` are `readonly` locators.
- `tests/login.spec.ts`: 5 tests (valid login, 3 bad-login rows, logout).
- `tests/nomination.spec.ts`: 3 tests (submit adds row, zero quantity rejected, two nominations update count).

**What changed**
- `BaseTest`, the driver setup and teardown, and every `WebDriverWait`, `ExpectedConditions` and custom wait are gone. Web-first assertions do the waiting.
- The `@DataProvider` became a loop that creates one test per row. Each test name includes the user and password, so the names are unique.
- `NominationTest`'s `@BeforeMethod` became a `test.beforeEach` that logs in. There are no shared variables, and each test creates its own page objects.
- The four cell assertions on the first row are one `toHaveText([...])` call on the row's cells, which checks each value in order.
- `rowCount() == 0` became `toHaveCount(0)`.
- `isShown()` became `expect(signInButton).toBeVisible()`.

**Locators:** I used `getByRole` and `getByLabel` where they fit. I used CSS for `#login-error`, `#nom-error`, `#welcome` and `#nom-count`, because they have no role or label I could target without ambiguity. No XPath.

**Failure check:** I changed three expected values (`Welcome, WRONG`, `99,999`, `3 nominations`). Three tests failed as expected, and I restored the values. The final run is green.

**Not converted:** nothing. I didn't touch `playwright.config.ts`, the app, or the Selenium code.
