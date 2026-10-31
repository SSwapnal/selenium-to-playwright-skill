import { type Page, type Locator } from '@playwright/test';

export class NominationsPage {
  readonly heading: Locator;
  readonly welcome: Locator;
  readonly logoutButton: Locator;
  readonly pipeline: Locator;
  readonly quantity: Locator;
  readonly submitButton: Locator;
  readonly error: Locator;
  readonly toast: Locator;
  readonly rows: Locator;
  readonly count: Locator;

  constructor(readonly page: Page) {
    this.heading = page.getByRole('heading', { name: 'Nominations' });
    this.welcome = page.locator('#welcome');
    this.logoutButton = page.getByRole('button', { name: 'Log out' });
    this.pipeline = page.getByLabel('Pipeline');
    this.quantity = page.getByLabel('Quantity (Dth)');
    this.submitButton = page.getByRole('button', { name: 'Submit nomination' });
    this.error = page.locator('#nom-error');
    this.toast = page.getByRole('status');
    this.rows = page.locator('#nom-table tbody').getByRole('row');
    this.count = page.locator('#nom-count');
  }

  async submit(pipelineName: string, qty: string) {
    await this.pipeline.selectOption(pipelineName);
    await this.quantity.fill(qty);
    await this.submitButton.click();
  }

  async logout() {
    await this.logoutButton.click();
  }
}
