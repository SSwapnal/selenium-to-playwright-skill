import { defineConfig, devices } from '@playwright/test';
import path from 'path';
import { pathToFileURL } from 'url';

export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  retries: 0,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    // The practice app is a local file: page.goto('index.html') opens it.
    baseURL: pathToFileURL(path.join(__dirname, '../app/')).href,
    trace: 'retain-on-failure',
  },
  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
});
