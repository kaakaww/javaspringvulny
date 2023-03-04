import { PlaywrightTestConfig, ViewportSize, PlaywrightTestProject } from "@playwright/test";
import { accountStoragePath, allTestAccounts, TestAccount } from "./accounts";
import process from "process";
import path from "path";
import dotenv from 'dotenv';

export const parsedConfig = dotenv.config().parsed as Record<string, string>;
export const appHost = (): string => parsedConfig["APP_TEST_HOST"];
export const httpProxy = (): string => process.env.HTTP_PROXY || ''

type PlaywrightProject = PlaywrightTestProject & { metadata: TestAccount };

const testProjects: PlaywrightTestProject[] = [
  {
    name: "Desktop-Chromium",
    use: {
      browserName: "chromium",
      viewport: { width: 1280, height: 720 },
    },
  }
  // {
  //   name: 'Mobile Chrome',
  //   use: devices['Pixel 5'],
  // },
  // {
  //   name: 'Mobile Safari',
  //   use: devices['iPhone 12'],
  // },
];

const playwrightProjects: PlaywrightProject[] = testProjects.flatMap(
  (testProject: PlaywrightTestProject) =>
    allTestAccounts.map((account) => {
      const name = `${testProject.name}:${account.name}`;
      const storageState = accountStoragePath(account);
      const use = { ...testProject.use, storageState };
      const metadata = { ...account as TestAccount };
      const project : PlaywrightProject = { ...testProject, metadata, use, name }
      return project
    })
);

/** 
 * NOTE: proxy configuration is required for custom Scan Discovery. This will selectively enable it only if
 * the HTTP_PROXY environment variable is present when HawkScan runs.
 */
const proxy = httpProxy() ? { server: httpProxy() } : undefined;

const config: PlaywrightTestConfig<PlaywrightProject> = {
  forbidOnly: true,
  globalSetup: path.join(__dirname, "global-setup.ts"),
  testIgnore: "src/**",
  retries: 0,
  use: {
    proxy, // This is required for Playwright scan discovery! 
    ignoreHTTPSErrors: true,
    video: "on-first-retry",
    baseURL: appHost(),
    javaScriptEnabled: true,
    screenshot: "only-on-failure",
  },
  projects: playwrightProjects,
  reporter: [
    ["list"]
  ],
};
export default config;
