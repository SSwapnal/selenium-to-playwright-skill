import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';
import { NominationsPage } from '../pages/NominationsPage';

test.beforeEach(async ({ page }) => {
  await page.goto('index.html');
  await new LoginPage(page).login('scheduler', 'Gas#2026');
  await expect(new NominationsPage(page).heading).toBeVisible();
});

test('submit nomination adds row', async ({ page }) => {
  const nominations = new NominationsPage(page);
  await nominations.submit('Transco', '10000');
  await expect(nominations.toast).toHaveText('Nomination N-1001 submitted');
  await expect(nominations.rows).toHaveCount(1);
  await expect(nominations.rows.nth(0).getByRole('cell')).toHaveText([
    'N-1001',
    'Transco',
    '10,000',
    'Submitted',
  ]);
});

test('zero quantity is rejected', async ({ page }) => {
  const nominations = new NominationsPage(page);
  await nominations.submit('Texas Eastern', '0');
  await expect(nominations.error).toHaveText('Quantity must be greater than 0');
  await expect(nominations.rows).toHaveCount(0);
});

test('two nominations update count', async ({ page }) => {
  const nominations = new NominationsPage(page);
  await nominations.submit('Transco', '5000');
  await expect(nominations.rows).toHaveCount(1);
  await nominations.submit('ANR Pipeline', '7500');
  await expect(nominations.rows).toHaveCount(2);
  await expect(nominations.count).toHaveText('2 nominations');
});
