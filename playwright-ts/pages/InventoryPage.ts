import { Page, Locator, expect } from '@playwright/test';

export class InventoryPage {
  readonly page: Page;
  readonly pageTitle: Locator;
  readonly backpackAddButton: Locator;
  readonly backpackRemoveButton: Locator;
  readonly cartBadge: Locator;
  readonly cartLink: Locator;

  constructor(page: Page) {
    this.page = page;
    this.pageTitle = page.locator('.title');
    this.backpackAddButton = page.getByTestId('add-to-cart-sauce-labs-backpack');
    this.backpackRemoveButton = page.getByTestId('remove-sauce-labs-backpack');
    this.cartBadge = page.locator('.shopping_cart_badge');
    this.cartLink = page.locator('.shopping_cart_link');
  }
  async expectLoaded() { await expect(this.pageTitle).toHaveText('Products'); }
  async addBackpack() { await this.backpackAddButton.click(); }
  async removeBackpack() { await this.backpackRemoveButton.click(); }
  async openCart() { await this.cartLink.click(); }
}
