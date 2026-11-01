import { test, expect } from '@playwright/test';
import { LoginPage } from './pages/SauceDemoPages';

test.describe('LoginTests (migrated from Selenium)', () => {
  test('TC01 - Verify login with valid credentials', async ({ page }) => {
    const login = new LoginPage(page);
    await login.open();
    await login.login('standard_user', 'secret_sauce');

    await expect(page.getByTestId('title')).toHaveText('Products');
    await expect(page).toHaveURL(/inventory\.html/);
  });

  test('TC02 - Verify invalid credentials show an error', async ({ page }) => {
    const login = new LoginPage(page);
    await login.open();
    await login.login('standard_user', 'wrong_password');

    await expect(login.errorMessage).toContainText('Username and password do not match');
    await expect(login.loginButton).toBeVisible();
  });
});
