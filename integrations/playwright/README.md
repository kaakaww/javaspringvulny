## [Playwright](https://www.playwright.dev/)

![Playwright](../images/playwright-banner.png)

Playwright is a frontend testing tool that automates the testing of web applications through the browser executables bundled with Playwright. Playwright test specs will execute as javascript running in the web application, selecting and testing elements on the page.

At StackHawk, _we like Playwright_, and encourage it's use for teams with complex browser requirements, or with established CI/CD pipelines that can support custom images, so they can take full advantage of Playwright.

Follow the [Playwright guide for getting started](https://playwright.dev/docs/intro).

### Testing with [javaspringvulny](https://github.com/kaakaww/javaspringvulny)

> Ensure javaspringvulny web application is running on `https://localhost:9000`

> Playwright requires first running `npx playwright install` to install browsers.

Headed: `npx playwright test -c playwright/out --config=playwright/playwright.config.ts --headed`

Headless: `npx playwright test -c playwright/out --config=playwright/playwright.config.ts`

Specific test: `npx playwright test specific.spec.ts -c playwright/out --config=playwright/playwright.config.ts`

#### Parallel Browser Testing

Playwright supports running tests across [multiple browsers in parallel](https://docs.cypress.io/guides/guides/parallelization#Turning-on-parallelization) by default.
 
### Adding Custom Authentication

Playwright gives the full javascript ecosystem to the developer, and encourages the creation of helper classes for testing.  [Custom commands](https://github.com/stackhawk/stackhawk-custom-image/blob/main/integrations/playwright/playwrightPage.ts) can be added to the helper class.

### Scanning with HawkScan

To use Playwright custom scan discovery, you will need to specify the [http proxy server setting](https://playwright.dev/docs/network#http-proxy) in your playwright config.

```ts
// Proxy setting should use the value from the HTTP_PROXY envvar
const httpProxy : string | undefined = process.env["HTTP_PROXY"];
// conditionally enable the proxy if the variable was present
const proxy = httpProxy ? { server: httpProxy } : undefined;
const config: PlaywrightTestConfig<PlaywrightProject> = {
  use: {
    proxy, // This is required for Playwright scan discovery! 
    ignoreHTTPSErrors: true,
    baseURL: process.env["APP_HOST"],
    javaScriptEnabled: true,
  },
  ...
};
```

See the docs for using [Playwright tests with Custom Scan Discovery](https://docs.stackhawk.com/hawkscan/scan-discovery/custom.html).

You can use the sample [playwright-stackhawk.yml](https://github.com/stackhawk/stackhawk-custom-image/blob/main/integrations/playwright/playwright-stackhawk.yml) file for an example of scanning a web application with it.

### Playwright Best Practices

* Playwright encourages creating an extension of the Page class with [test-fixtures](https://playwright.dev/docs/test-fixtures), that will use the resources and details of the tested page to perform custom commands. This is demonstrated with the [PlayWrightPage](https://github.com/stackhawk/stackhawk-custom-image/blob/main/integrations/playwright/playwrightPage.ts)

* Create a [global-setup](https://playwright.dev/docs/test-advanced#global-setup-and-teardown) function to authenticate and manage sessions used by your playwright tests for faster test setup and reuse.

* [Official Docker Images](https://playwright.dev/docs/docker)

* https://playwright.dev/docs/selectors#best-practices