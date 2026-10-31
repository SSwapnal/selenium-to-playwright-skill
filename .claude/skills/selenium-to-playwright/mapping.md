# Selenium → Playwright mapping

| Selenium (Java) | Playwright (TypeScript) |
|---|---|
| `driver.get(APP_URL)` | `await page.goto('index.html')` (baseURL is in the config) |
| `driver.findElement(By.id("username"))` | `page.getByLabel('Username')` |
| `By.xpath("//button[text()='Sign in']")` | `page.getByRole('button', { name: 'Sign in' })` |
| `By.id("x")` with no label or role | `page.locator('#x')` |
| `element.sendKeys("text")` | `await locator.fill('text')` |
| `element.clear()` | Not needed: `fill()` clears first |
| `element.click()` | `await locator.click()` |
| `new Select(el).selectByVisibleText("X")` | `await locator.selectOption('X')` |
| `WebDriverWait` + `ExpectedConditions.visibilityOf...` | Delete. `await expect(locator).toBeVisible()` |
| `wait.until(d -> !el.getText().isEmpty())` | Delete. `await expect(locator).toHaveText('...')` |
| `ExpectedConditions.numberOfElementsToBe(rows, 2)` | `await expect(rows).toHaveCount(2)` |
| `Thread.sleep(...)` | Delete |
| `Assert.assertEquals(el.getText(), "X")` | `await expect(locator).toHaveText('X')` |
| `Assert.assertTrue(el.isDisplayed())` | `await expect(locator).toBeVisible()` |
| `driver.findElements(rows).size() == 0` | `await expect(rows).toHaveCount(0)` |
| `findElements(rows).get(0).findElements(By.tagName("td"))` | `rows.nth(0).getByRole('cell')` |
| `@BeforeMethod` | `test.beforeEach(async ({ page }) => { ... })` |
| `@AfterMethod` / `driver.quit()` | Delete: Playwright closes the page |
| `@DataProvider` | `for (const row of data) { test(\`... ${row.x}\`, ...) }` |
| `BaseTest` with `WebDriver` setup | Delete: each test gets its own `page` |
| `ThreadLocal<WebDriver>` / Selenium Grid | Not needed: `workers` in the config run tests in parallel |

## Page object shape

```ts
import { type Page, type Locator } from '@playwright/test';

export class LoginPage {
  readonly username: Locator;

  constructor(readonly page: Page) {
    this.username = page.getByLabel('Username');
  }

  async login(user: string, pass: string) {
    await this.username.fill(user);
    // ...
  }
}
```
