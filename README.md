# SauceDemo: Selenium → Playwright AI Migration

A beginner-friendly portfolio project demonstrating:
1. Selenium WebDriver + Java + TestNG
2. Playwright + TypeScript
3. An AI migration assistant guided by a reusable Agent Skill (`SKILL.md`)

**Result:** an AI coding agent, guided by the skill, migrated all 5 Selenium tests to Playwright. All 5 pass on the live site. See [docs/ai-migration-report.md](docs/ai-migration-report.md).

Application: https://www.saucedemo.com/

## Architecture

```text
selenium-java/                 Original Java + TestNG suite
  src/test/java/base/           WebDriver setup
  src/test/java/pages/          Page Object Model
  src/test/java/tests/          Login and shopping tests
playwright-ts/                 Equivalent TypeScript suite
  pages/                        Page Object Model
  tests/                        Login and shopping tests (hand-written)
  tests/ai-migrated/            Same 5 tests, migrated by an AI agent using the skill
skills/selenium-to-playwright-migration/
  SKILL.md                      Skill metadata + migration workflow
  references/
    selenium-playwright-mapping.md
    migration-checklist.md       Detailed mappings + validation checklist
    mapping-notes.md             Concepts with no one-to-one mapping
.claude/skills/                Link to the skill, so coding agents discover it
agent/migrate.py               LLM-powered migration assistant (API script)
docs/ai-migration-report.md    What the agent migrated, and the executed results
.github/workflows/tests.yml    CI: runs the Selenium and Playwright suites
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
mvn clean test              # HEADLESS=true mvn clean test  to hide the browser
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

## 3. Migrate with an AI coding agent (uses the skill)

Coding agents such as Claude Code, GitHub Copilot and Codex read Agent Skills from the repository. Open the repo root in the agent and ask:

```text
Migrate the Selenium tests in selenium-java to Playwright.
Put the migrated specs in playwright-ts/tests/ai-migrated/ and do not change any existing Playwright files.
Save your migration report as docs/ai-migration-report.md.
```

The agent matches the request to the skill's `description` and loads `SKILL.md`. It then reads the Java sources and writes the specs, runs `tsc` and `npx playwright test`, and writes the report. Only the name and description are loaded up front; the reference files are read only when needed.

Results of the run committed here:

| Step | Result |
|---|---|
| Skill loaded automatically | Yes (`selenium-to-playwright-migration`) |
| Tests migrated | 5 of 5, with all assertions kept |
| `npx tsc --noEmit` | Passed |
| `npx playwright test tests/ai-migrated` | 5 passed |

## 4. Run the AI migration assistant (API script)

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

Never commit API keys. The `.gitignore` excludes `.env` and Python virtual environments.

##  Explanation

“I built an end-to-end automation migration demo around SauceDemo. First, I created the source regression suite using Selenium, Java, TestNG, and Page Objects. Then I implemented the same user journeys in Playwright with TypeScript. Finally, I wrote a reusable Agent Skill that gives an AI agent explicit migration rules, and a small Python runner that passes the skill and Java source to an LLM. The generated TypeScript is reviewed and executed, and I compare the scenarios and assertions across both suites. The goal is to accelerate repetitive migration work while keeping a human review and test execution as quality gates.”
