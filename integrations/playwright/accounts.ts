/**
 * Represents a user logging in with email and credentials
 */
import * as path from "path";
import {TestInfo} from "@playwright/test";

export type TestAccount = TestInfo & {
    // the name of this user
    name?: string;
    // the account username
    username?: string;
    // the account password
    password?: string;
}

export const allTestAccounts: Partial<TestAccount>[] = [{
    name: 'defaultUser',
    username: 'user',
    password: 'password'
}];

const authStoragePath = 'auth';

export const accountStoragePath = (account: Partial<TestAccount>): string =>
    path.join(__dirname, authStoragePath, `${account.name}.json`);