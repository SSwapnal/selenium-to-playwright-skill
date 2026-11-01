import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';
import { InventoryPage } from '../pages/InventoryPage';
import { CartPage } from '../pages/CartPage';

async function signIn(page: import('@playwright/test').Page) {
  const login = new LoginPage(page);
  await login.open();
  await login.login('standard_user', 'secret_sauce');
}

test('add backpack and verify cart contents', async ({ page }) => {
  await signIn(page);
  const inventory = new InventoryPage(page);
  await inventory.expectLoaded();
  await inventory.addBackpack();
  await expect(inventory.cartBadge).toHaveText('1');
  await inventory.openCart();
  await new CartPage(page).expectBackpackInCart();
});

test('remove backpack from cart', async ({ page }) => {
  await signIn(page);
  const inventory = new InventoryPage(page);
  await inventory.addBackpack();
  await inventory.openCart();
  const cart = new CartPage(page);
  await cart.expectBackpackInCart();
  await cart.removeBackpack();
  await expect(page.locator('.cart_item')).toHaveCount(0);
});

test('complete checkout for backpack', async ({ page }) => {
  await signIn(page);
  const inventory = new InventoryPage(page);
  await inventory.addBackpack();
  await inventory.openCart();
  const cart = new CartPage(page);
  await cart.expectBackpackInCart();
  await cart.checkout();
  await page.getByTestId('firstName').fill('Swapnal');
  await page.getByTestId('lastName').fill('Tester');
  await page.getByTestId('postalCode').fill('94086');
  await page.getByTestId('continue').click();
  await page.getByTestId('finish').click();
  await expect(page.locator('.complete-header')).toHaveText('Thank you for your order!');
});
