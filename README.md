# Selenium → Playwright with an AI skill

A small Selenium suite, and an **AI agent skill** that converts it to Playwright.

The skill is a short instruction file in `.claude/skills/`. When you ask the agent to convert a test, it loads the skill and follows the same steps and rules every time.

```
app/                 Practice app: login + gas nominations (one HTML file, fictional)
selenium/            Before: Selenium 4 + TestNG + Java, page objects     8 tests, ~18 s
playwright/          After: Playwright + TypeScript, made by the skill    8 tests, ~3 s
.claude/skills/
  selenium-to-playwright/
    SKILL.md         Steps + rules the agent follows
    mapping.md       Selenium → Playwright cheat sheet
docs/
  conversion-report.md   What the agent did, and how the skill was improved
```

## Run

No server and no internet needed. The app is a local file.

```bash
# Selenium (Java 17+, Maven, Chrome)
cd selenium && mvn test

# Playwright (Node 20+)
cd playwright && npm install && npx playwright install chromium && npx playwright test
```

## Convert with the skill

In [Claude Code](https://docs.claude.com/en/docs/claude-code), from the repo root:

```text
Convert the Selenium tests in this repo to Playwright.
```

The agent:

1. Matches the request to the skill's description and loads `SKILL.md`.
2. Reads the tests and page objects.
3. Converts them using `mapping.md`.
4. Runs them until they pass.
5. Breaks one expected value to prove the test can fail.
6. Reports back.

On this suite it took about 70 seconds.

## Before → after

| Selenium | Playwright |
|---|---|
| `By.xpath("//button[text()='Sign in']")` | `getByRole('button', { name: 'Sign in' })` |
| `WebDriverWait` + `ExpectedConditions` | Gone. `expect(...)` waits by itself |
| `Assert.assertEquals(el.getText(), "X")` | `await expect(locator).toHaveText('X')` |
| `@DataProvider` | `for` loop, one `test()` per row |
| `BaseTest` driver setup / quit | Gone. Each test gets a fresh `page` |

## Why a local practice app

The first version targeted a public demo store. There, Selenium clicks were sometimes lost while menus animated. Playwright passed 10 out of 10 because it waits for an element to be stable before clicking. A local app keeps this demo deterministic.
