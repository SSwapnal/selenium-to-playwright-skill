import { Page, Locator, expect } from '@playwright/test';

export class LoginPage {
  readonly username: Locator;
  readonly password: Locator;
  readonly loginButton: Locator;
  readonly errorMessage: Locator;

  constructor(private readonly page: Page) {
    this.username = page.getByTestId('username');
    this.password = page.getByTestId('password');
    this.loginButton = page.getByTestId('login-button');
    this.errorMessage = page.getByTestId('error');
  }

  async open() {
    await this.page.goto('/');
  }

  async login(user: string, pass: string) {
    await this.username.fill(user);
    await this.password.fill(pass);
    await this.loginButton.click();
  }
}

export class InventoryPage {
  readonly title: Locator;
  readonly cartBadge: Locator;
  readonly cartLink: Locator;
  readonly backpackAdd: Locator;

  constructor(private readonly page: Page) {
    this.title = page.getByTestId('title');
    this.cartBadge = page.getByTestId('shopping-cart-badge');
    this.cartLink = page.getByTestId('shopping-cart-link');
    this.backpackAdd = page.getByTestId('add-to-cart-sauce-labs-backpack');
  }

  async addBackpack() {
    await this.backpackAdd.click();
    await expect(this.cartBadge).toBeVisible();
  }

  async openCart() {
    await this.cartLink.click();
    await expect(this.page.getByTestId('checkout')).toBeVisible();
  }
}

export class CartPage {
  readonly items: Locator;
  readonly itemName: Locator;
  readonly removeButton: Locator;
  readonly checkoutButton: Locator;

  constructor(private readonly page: Page) {
    this.items = page.getByTestId('inventory-item');
    this.itemName = page.getByTestId('inventory-item-name');
    this.removeButton = page.getByTestId('remove-sauce-labs-backpack');
    this.checkoutButton = page.getByTestId('checkout');
  }

  async removeItem() {
    await this.removeButton.click();
  }

  async checkout() {
    await this.checkoutButton.click();
    await expect(this.page.getByTestId('firstName')).toBeVisible();
  }
}

export async function signIn(page: Page) {
  const login = new LoginPage(page);
  await login.open();
  await login.login('standard_user', 'secret_sauce');
  await expect(page.getByTestId('title')).toBeVisible();
}
