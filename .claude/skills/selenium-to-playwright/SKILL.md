---
name: selenium-to-playwright
description: Convert Selenium Java + TestNG tests and page objects to Playwright TypeScript.
  Use when asked to migrate, port or convert a Selenium test, test class or page object.
---

# Selenium → Playwright

Input: Selenium tests in `selenium/src/test/java/`.
Output: Playwright project in `playwright/` (config already set up).

## Steps
1. Read the test class, its `BaseTest` and every page object it uses.
2. Convert with the table in [mapping.md](mapping.md).
   - Page objects → `playwright/pages/<Name>.ts`
   - Test classes → `playwright/tests/<name>.spec.ts` (kebab-case, e.g. `login.spec.ts`)
3. Run: `cd playwright && npx playwright test <spec>`. Fix until it passes.
4. Check each test can fail: break one expected value, run, see red, put it back.
5. Report: files created, what changed, anything not converted.

## Rules
- Locators, in this order: `getByRole` → `getByLabel` → `getByText` → `getByTestId` → CSS.
  Use XPath only if nothing else works, and say so in the report.
- Remove every `WebDriverWait`, `ExpectedConditions`, `Thread.sleep` and custom wait loop.
  Use web-first assertions (`await expect(locator).toHaveText(...)`); they wait by themselves.
- Never `expect(await locator.textContent())`. Assert on the locator.
- Keep every assertion and its expected value. Never weaken or delete one.
- One `@Test` = one `test()`. Keep the test names (as readable sentences).
- Page objects: actions (`login`, `submit`, `logout`) stay `async` methods with the same names.
  Getters that only returned text or a check (`errorText()`, `heading()`) become
  `readonly` Locator properties (`error`, `heading`). No method that just returns a locator.
- No `let` variables shared between tests. Create page objects inside each test,
  or in a fixture.
- Don't change `playwright.config.ts`, the app, or the Selenium code.
- Don't invent data, URLs or features. If something is unclear, stop and ask.
