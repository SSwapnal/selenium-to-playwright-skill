# Selenium Java to Playwright TypeScript Mapping

> Part of the [selenium-to-playwright-migration](../SKILL.md) skill.

## Core API mapping

| Selenium Java + TestNG | Playwright TypeScript |
|---|---|
| `WebDriver driver` | `Page page` supplied by the Playwright Test fixture |
| `driver.get(url)` | `await page.goto(url)` |
| `By.id("username")` | `page.locator('#username')` or a semantic locator when available |
| `By.name("q")` | `page.locator('[name="q"]')` |
| `By.cssSelector("...")` | `page.locator('...')` |
| `By.xpath("...")` | `page.locator('xpath=...')`; prefer semantic locators if reliable |
| `findElement(locator)` | `page.locator(...)` |
| `element.click()` | `await locator.click()` |
| `element.sendKeys(value)` | `await locator.fill(value)` for normal form entry |
| `element.clear()` | `await locator.clear()` |
| `element.getText()` | `await locator.innerText()` or a web-first text assertion |
| `element.isDisplayed()` | `await expect(locator).toBeVisible()` |
| `element.isEnabled()` | `await expect(locator).toBeEnabled()` |
| `Select` + `selectByVisibleText` | `await locator.selectOption({ label: '...' })` |
| `Select` + `selectByValue` | `await locator.selectOption('value')` |
| `WebDriverWait` + `ExpectedConditions` | Locator actions and retrying `expect` assertions; use explicit locator waits only when needed |
| `Assert.assertEquals(actual, expected)` | `expect(actual).toBe(expected)` or `await expect(locator).toHaveText(expected)` |
| `Assert.assertTrue(condition)` | `expect(condition).toBeTruthy()` or a locator assertion |
| `Assert.assertFalse(condition)` | `expect(condition).toBeFalsy()` or `await expect(locator).not.toBeVisible()` |
| `@BeforeMethod` | `test.beforeEach` or a fixture |
| `@AfterMethod` / `driver.quit()` | Playwright Test manages browser/page lifecycle |
| TestNG `@Test` | `test('name', async ({ page }) => { ... })` |

## Locator selection order

1. `getByRole` with an accessible name.
2. `getByLabel` for form controls.
3. `getByPlaceholder` when the placeholder is stable and meaningful.
4. `getByTestId` when the application provides test IDs.
5. CSS selector for stable IDs or attributes.
6. XPath only when other options are unsuitable.

Avoid absolute XPath, positional selectors such as `nth()` when a semantic filter is possible, and styling classes that are likely to change.

## Wait and assertion principles

- Do not add `waitForTimeout()` as a direct replacement for Selenium explicit waits.
- Playwright actions auto-wait for actionability conditions.
- Prefer retrying assertions such as `toHaveURL`, `toBeVisible`, `toHaveText`, and `toHaveCount`.
- Use `locator.waitFor()` only when a specific wait is required and an assertion is not the better expression of the expectation.

## Example: login conversion

Selenium:

```java
 driver.findElement(By.id("user-name")).sendKeys("standard_user");
 driver.findElement(By.id("password")).sendKeys("secret_sauce");
 driver.findElement(By.id("login-button")).click();
 Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
```

Playwright:

```ts
await page.getByTestId('username').fill('standard_user');
await page.getByTestId('password').fill('secret_sauce');
await page.getByTestId('login-button').click();
await expect(page).toHaveURL(/inventory/);
```

Confirm the test IDs against the actual SauceDemo DOM before using this example in a live migration. If they are not present, choose stable CSS or accessible locators.
