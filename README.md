# SauceDemo: Selenium → Playwright AI Migration

A beginner-friendly portfolio project demonstrating:
1. Selenium WebDriver + Java + TestNG
2. Playwright + TypeScript
3. An AI migration assistant guided by a reusable Agent Skill (`SKILL.md`)

Application: https://www.saucedemo.com/

## Architecture

```text
selenium-java/                 Original Java + TestNG suite
  src/test/java/base/           WebDriver setup
  src/test/java/pages/          Page Object Model
  src/test/java/tests/          Login and shopping tests
playwright-ts/                 Equivalent TypeScript suite
  pages/                        Page Object Model
  tests/                        Login and shopping tests
skills/selenium-to-playwright-migration/
  SKILL.md                      Skill metadata + migration workflow
  references/
    selenium-playwright-mapping.md
    migration-checklist.md       Detailed mappings + validation checklist
agent/migrate.py               LLM-powered migration assistant
```

## Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 20+
- Python 3.10+ (only for the AI migration script)
- Chrome or Chromium

## 1. Run Selenium Java + TestNG

```bash
cd selenium-java
mvn clean test
```

Selenium 4 uses Selenium Manager to obtain a compatible browser driver in normal setups. If your environment blocks driver downloads, configure ChromeDriver manually or allow the download.

## 2. Run Playwright TypeScript

```bash
cd ../playwright-ts
npm install
npx playwright install chromium
npx playwright test
```

Useful commands:
```bash
npx playwright test --headed
npx playwright test --ui
npx playwright show-report
```

## 3. Run the AI migration assistant

The assistant reads the skill, a Selenium source file, and asks an LLM to produce a TypeScript Playwright test. It does not silently overwrite your existing tests.

```bash
cd ../agent
python3 -m venv .venv
source .venv/bin/activate       # Windows: .venv\\Scripts\\activate
pip install -r requirements.txt
export OPENAI_API_KEY="your-api-key"
python migrate.py \
  --input ../selenium-java/src/test/java/tests/LoginTests.java \
  --output ../playwright-ts/tests/ai-migrated-login.spec.ts
```

Optional model selection:
```bash
export OPENAI_MODEL="gpt-4.1-mini"
```

Review the generated file before running it. Then:
```bash
cd ../playwright-ts
npx playwright test tests/ai-migrated-login.spec.ts
```

The migration assistant is a practical starter, not a guarantee of semantic equivalence. AI-generated code must be reviewed and executed. Complex Java helpers, custom waits, test data, and framework hooks may need manual mapping.

## Test scenarios covered

| Scenario | Selenium/TestNG | Playwright |
|---|---:|---:|
| Valid login | Yes | Yes |
| Invalid login | Yes | Yes |
| Add product to cart | Yes | Yes |
| Verify cart contents | Yes | Yes |
| Remove product | Yes | Yes |
| Checkout happy path | Yes | Yes |

## Push this project to your GitHub

Create an empty GitHub repository named `saucedemo-ai-test-migration`, then run from this folder:

```bash
git init
git add .
git commit -m "Add Selenium to Playwright AI migration project"
git branch -M main
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/saucedemo-ai-test-migration.git
git push -u origin main
```

Do not commit API keys. The `.gitignore` excludes `.env` and Python virtual environments.

## Interview explanation

“I built an end-to-end automation migration demo around SauceDemo. First, I created the source regression suite using Selenium, Java, TestNG, and Page Objects. Then I implemented the same user journeys in Playwright with TypeScript. Finally, I wrote a reusable Agent Skill that gives an AI agent explicit migration rules, and a small Python runner that passes the skill and Java source to an LLM. The generated TypeScript is reviewed and executed, and I compare the scenarios and assertions across both suites. The goal is to accelerate repetitive migration work while keeping a human review and test execution as quality gates.”
