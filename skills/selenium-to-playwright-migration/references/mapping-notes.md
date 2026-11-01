# Migration notes

- Selenium uses a WebDriver command model. Playwright Test provides a `page` fixture and built-in waiting around actions and assertions.
- Selenium `WebElement` references are not direct equivalents of Playwright `Locator`. A Locator is a query that can resolve again when used, which helps with dynamic pages.
- Selenium's `Select` class is for native HTML `<select>` controls. In Playwright, use `locator.selectOption(...)`; for custom dropdowns, interact with the UI using locators.
- TestNG data providers can often become Playwright parameterized tests or `test.describe` loops.
- Review thread-local drivers, static state, custom listeners, retry analyzers, downloads, windows, alerts, and Java-specific utilities individually; they do not have automatic one-to-one translations.
