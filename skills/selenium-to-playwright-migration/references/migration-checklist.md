# Migration Checklist and Validation Report

> Part of the [selenium-to-playwright-migration](../SKILL.md) skill.

## Before migration

- [ ] Read the complete Selenium test and all referenced classes.
- [ ] Identify base URL, browser setup, setup/teardown, and test data.
- [ ] Record each test's actions and expected results.
- [ ] Identify shared state, dependencies, and custom wait logic.
- [ ] Inspect the existing Playwright configuration and conventions.

## During migration

- [ ] Preserve scenario intent and assertions.
- [ ] Choose stable, user-facing locators where possible.
- [ ] Convert waits to Playwright auto-waiting and web-first assertions.
- [ ] Use async/await consistently.
- [ ] Reuse or create Page Objects consistently.
- [ ] Keep tests independent and avoid hidden ordering dependencies.
- [ ] List assumptions and behavior that needs manual attention.

## After migration

- [ ] Review generated TypeScript and imports.
- [ ] Run TypeScript checks if configured (`npx tsc --noEmit`).
- [ ] Run the migrated test (`npx playwright test path/to/test.spec.ts`).
- [ ] Compare scenario coverage and assertions with the Selenium source.
- [ ] Record actual pass/fail/blocked status and evidence.

## Suggested report format

| Source test | Target test | Status | Notes |
|---|---|---|---|
| `LoginTests.java` | `login.spec.ts` | Not run / Passed / Failed / Blocked | Record differences or evidence |

Never label a generated test as passing until it has been executed successfully.
