/**
 * PlaywrightPage is a custom playwright utility class with helper functions for complex tests.
 * These helper functions use the given page and TestAccount to make simpler tests
 * @export
 * @class PlaywrightPage
 */

import {Page, expect} from "@playwright/test";
import {TestAccount} from "./accounts";
import { config } from 'dotenv';

export const parsedConfig = config().parsed as Record<string, string>;
export const appHost = (): string => parsedConfig['APP_TEST_HOST'];
export const tokenName = (): string => parsedConfig['TOKEN_NAME'];
export const tokenValue = (): string => parsedConfig['TOKEN_VALUE'];

export class PlaywrightPage {
    public readonly page: Page;
    public readonly account: TestAccount;

    public constructor(page: Page, details: TestAccount) {
        this.page = page;
        this.account = details.project.metadata as TestAccount
    }

    public async formAuth(): Promise<void> {
        await this.page.goto(`${appHost()}/login`, { waitUntil: 'networkidle' });
        await this.page.locator('#username').fill(this.account.username)
        await this.page.locator('#password').fill(this.account.password)
        await this.page.locator('.btn >> text=Submit').click();
    }

    public async jwtAuth(): Promise<void> {
        await this.page.goto(`${appHost()}/jwt-auth`, { waitUntil: 'networkidle' });
        await this.page.locator('button[data-toggle="modal"]').click()
        expect(await this.page.locator('.modal-dialog').isVisible())
        await this.page.locator('#username').fill(this.account.username)
        await this.page.locator('#password').fill(this.account.password)
        await this.page.locator('button#login').click();
        expect((await this.page.locator('#login-message').textContent()).includes('200 OK'))
        await this.page.locator('button.btn-secondary').click();
    }

    public async tokenAuth(): Promise<void> {
        await this.page.goto(`${appHost()}/token-auth`, { waitUntil: 'networkidle' });
        await this.page.locator('#token-name').fill(tokenName())
        await this.page.locator('#token-value').fill(tokenValue())
    }

    public async basicAuth(): Promise<void> {
        await this.page.goto(`${appHost()}/basic-auth`, { waitUntil: 'networkidle' });
        await this.page.locator('#username').fill(this.account.username)
        await this.page.locator('#password').fill(this.account.password)
    }

    public async formMultiAuth(): Promise<void> {
        await this.page.goto(`${appHost()}/login-form-multi`, { waitUntil: 'networkidle' });
        await this.page.locator('#username').fill(this.account.username)
        await this.page.locator('#password').fill(this.account.password)
        await this.page.locator('#remember').click()
        await this.page.locator('.btn >> text=Submit').click()
    }

    public async attemptSearch(value: string): Promise<void> {
        const searchButton = '#items'
        const resultsPane = '#results'
        const searchField = '#search'
        const sb = await this.page.locator(searchButton)
        const sf = await this.page.locator(searchField)
        if(value && await sf.count() > 0) {
            await sf.fill(value);
        }
        if (await sb.count() > 0) {
            await sb.click()
            await expect(this.page.locator(resultsPane)).toHaveClass('alert-success')
        }

    }

    public async signOut(): Promise<void> {
        const signoutButton = 'button[type="button"].btn-primary'
        const logoutButton = '#logout'
        const sb = await this.page.locator(signoutButton)
        if (await sb.count() === 1 && (await sb.textContent()).includes("Sign Out")) {
            await this.page.locator(signoutButton).click();
        }
        const lb = await this.page.locator(logoutButton)
        if (await lb.count() === 1) {
            await this.page.locator(logoutButton).click();
        }
    }
}