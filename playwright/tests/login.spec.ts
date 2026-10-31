import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';
import { NominationsPage } from '../pages/NominationsPage';

test.beforeEach(async ({ page }) => {
  await page.goto('index.html');
});

test('valid login shows nominations', async ({ page }) => {
  await new LoginPage(page).login('scheduler', 'Gas#2026');
  const nominations = new NominationsPage(page);
  await expect(nominations.heading).toHaveText('Nominations');
  await expect(nominations.welcome).toHaveText('Welcome, scheduler');
});

const badLogins = [
  { user: 'scheduler', pass: 'wrong', expected: 'Invalid username or password' },
  { user: 'locked', pass: 'Gas#2026', expected: 'Your account is locked. Contact support.' },
  { user: '', pass: 'Gas#2026', expected: 'Username is required' },
];

for (const row of badLogins) {
  test(`bad login shows error for user "${row.user}" and password "${row.pass}"`, async ({ page }) => {
    const login = new LoginPage(page);
    await login.login(row.user, row.pass);
    await expect(login.error).toHaveText(row.expected);
  });
}

test('logout returns to sign in', async ({ page }) => {
  const login = new LoginPage(page);
  await login.login('scheduler', 'Gas#2026');
  const nominations = new NominationsPage(page);
  await expect(nominations.heading).toBeVisible();
  await nominations.logout();
  await expect(login.signInButton).toBeVisible();
});
