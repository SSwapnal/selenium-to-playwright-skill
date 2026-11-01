import { Page, Locator, expect } from '@playwright/test';

export class CartPage {
  readonly page: Page;
  readonly backpackName: Locator;
  readonly removeButton: Locator;
  readonly checkoutButton: Locator;
  constructor(page: Page) {
    this.page = page;
    this.backpackName = page.getByText('Sauce Labs Backpack', { exact: true });
    this.removeButton = page.getByTestId('remove-sauce-labs-backpack');
    this.checkoutButton = page.getByTestId('checkout');
  }
  async expectBackpackInCart() { await expect(this.backpackName).toBeVisible(); }
  async removeBackpack() { await this.removeButton.click(); }
  async checkout() { await this.checkoutButton.click(); }
}
