import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';

test('valid user can log in', async ({ page }) => {
  const login = new LoginPage(page);
  await login.open();
  await login.login('standard_user', 'secret_sauce');
  await expect(page).toHaveURL(/inventory\.html/);
  await expect(page.locator('.title')).toHaveText('Products');
});

test('invalid password displays an error', async ({ page }) => {
  const login = new LoginPage(page);
  await login.open();
  await login.login('standard_user', 'wrong_password');
  await login.expectLoginError('Username and password do not match');
});
