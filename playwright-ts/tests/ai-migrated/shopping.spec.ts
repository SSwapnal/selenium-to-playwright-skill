import { test, expect } from '@playwright/test';
import { CartPage, InventoryPage, signIn } from './pages/SauceDemoPages';

test.describe('ShoppingTests (migrated from Selenium)', () => {
  test.beforeEach(async ({ page }) => {
    await signIn(page);
  });

  test('TC03 - Add backpack and verify cart', async ({ page }) => {
    const inventory = new InventoryPage(page);
    await expect(inventory.title).toHaveText('Products');
    await inventory.addBackpack();
    await expect(inventory.cartBadge).toHaveText('1');
    await inventory.openCart();

    const cart = new CartPage(page);
    await expect(cart.itemName).toHaveText('Sauce Labs Backpack');
  });

  test('TC04 - Remove backpack from cart', async ({ page }) => {
    const inventory = new InventoryPage(page);
    await inventory.addBackpack();
    await inventory.openCart();

    const cart = new CartPage(page);
    await expect(cart.itemName).toHaveText('Sauce Labs Backpack');
    await cart.removeItem();

    await expect(cart.items).toHaveCount(0);
  });

  test('TC05 - Complete backpack checkout', async ({ page }) => {
    const inventory = new InventoryPage(page);
    await inventory.addBackpack();
    await inventory.openCart();

    const cart = new CartPage(page);
    await expect(cart.itemName).toHaveText('Sauce Labs Backpack');
    await cart.checkout();

    await page.getByTestId('firstName').fill('Swapnal');
    await page.getByTestId('lastName').fill('Tester');
    await page.getByTestId('postalCode').fill('94086');
    await page.getByTestId('continue').click();

    await expect(page.getByTestId('finish')).toBeVisible();
    await expect(page.getByTestId('inventory-item-name')).toHaveText('Sauce Labs Backpack');
    await page.getByTestId('finish').click();

    await expect(page.getByTestId('complete-header')).toHaveText('Thank you for your order!');
    await expect(page).toHaveURL(/checkout-complete\.html/);
  });
});
