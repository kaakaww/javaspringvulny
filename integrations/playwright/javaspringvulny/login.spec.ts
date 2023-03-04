import { test, expect } from '@playwright/test';
import { PlaywrightPage } from '../playwrightPage'

test.describe('login', () => {

    test('can login with formAuth', async ({ page }, workerInfo) => {
        const pwPage = new PlaywrightPage(page, workerInfo);
        await pwPage.formAuth();
        expect(pwPage.page.locator('button.btn-primary').isVisible());
    });

    test('can login with jwtAuth', async ({ page }, workerInfo) => {
        const pwPage = new PlaywrightPage(page, workerInfo);
        await pwPage.jwtAuth();
        await pwPage.attemptSearch('test');
    });

    test('can login with tokenAuth', async ({ page }, workerInfo) => {
        const pwPage = new PlaywrightPage(page, workerInfo);
        await pwPage.tokenAuth();
        await pwPage.attemptSearch('test');
        expect(pwPage.page.locator('#results')).toHaveClass('alert-success');
    });

    test('can login with basicAuth', async ({ page }, workerInfo) => {
        const pwPage = new PlaywrightPage(page, workerInfo);
        await pwPage.basicAuth();
        await pwPage.attemptSearch('test');
        expect(pwPage.page.locator('#results')).toHaveClass('alert-success');
    });

    test('can login with formMultiAuth', async ({ page }, workerInfo) => {
        const pwPage = new PlaywrightPage(page, workerInfo);
        await pwPage.formMultiAuth();
        await pwPage.page.goto('/search', { waitUntil: 'networkidle' });
        expect(pwPage.page.locator('#search').isVisible());
    });

    test.afterEach(async ({ page }, workerInfo) => {
        const pwPage = new PlaywrightPage(page, workerInfo);
        await pwPage.signOut();
    });
});
