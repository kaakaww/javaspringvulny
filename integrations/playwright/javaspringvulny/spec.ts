import { test, expect } from '@playwright/test';
import { appHost, PlaywrightPage } from '../playwrightPage'

test.describe('spec', async () => {
    test('can visit', async ({ page }, workerInfo) => {
        const pwPage = new PlaywrightPage(page, workerInfo);
        await pwPage.page.goto(`${appHost()}/hidden/playwright`, { waitUntil: 'networkidle' });
        expect(await pwPage.page.title()).toContain('playwright tests');
    });
});