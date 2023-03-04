# Testing Integrations

These are demonstrations of industry recognized developer tools used to automate E2E browser testing. The E2E tests are all meant to be run against [**javaspringvulny**](https://github.com/kaakaww/javaspringvulny), an intentionally vulnerable web application, running on `https://localhost:9000`

See how these E2E testing tools can be combined with HawkScan with [Custom Scan Discovery](https://docs.stackhawk.com/hawkscan/scan-discovery/custom.html).

* [**Cypress**](https://github.com/stackhawk/stackhawk-custom-image/tree/main/integrations/cypress)
* [**Playwright**](https://github.com/stackhawk/stackhawk-custom-image/tree/main/integrations/playwright)
* [**Selenium**](https://github.com/stackhawk/stackhawk-custom-image/tree/main/integrations/selenium)

## Building Custom Docker Images

You can run the following commands from this directory to build custom docker images with hawkscan and these tests

## Authentication

To perform E2E testing on an application, these testing tools will require scripted authentication into the application. HawkScan provides [common patterns for Authentication](https://docs.stackhawk.com/hawkscan/authenticated-scanning/), and **javaspringvulny** demonstrates multiple forms of authentication.

#### form authentication

Demonstrated on the `/login` page in JavaSpringVulny.

A common way to authenticate to a web application is by `POST`ing a username and password which can be verified by your server. Upon verification the server returns a cookie or token to the requesting client.

See [Form with Username + Password](https://docs.stackhawk.com/hawkscan/authenticated-scanning/form-based-authentication.html) for more information.

#### jwt token authentication

Demonstrated on the `/jwt-auth` page in JavaSpringVulny.

[Oauth 2.0 connections](https://oauth.net/2/) will require APIKeys and bearer tokens passed into their request headers. This type of authentication scheme is common for modern web APIs.

See [Cookie and Token](https://docs.stackhawk.com/hawkscan/authenticated-scanning/inject-cookies-and-tokens.html) for more information.

#### token authentication

Demonstrated on the `/token-auth` page in JavaSpringVulny.

Some web applications require supplying an authorization token which can be used in conjunction with either a token or a cookie to maintain the session.

See [External Token Authentication](https://docs.stackhawk.com/hawkscan/authenticated-scanning/inject-cookies-and-tokens.html#external-token-authentication--custom-token-authorization) for more information.

#### basic authentication

Demonstrated on the `/basic-auth` page in JavaSpringVulny.

> :warning: The "Basic" authentication scheme sends credentials encoded but not encrypted. This authentication scheme is insecure unless the exchange is over a secure connection (HTTPS/TLS). It is not recommended for production web APIs.

#### multi-auth form authentication

Demonstrated on the `/login-form-multi` page in JavaSpringVulny.

This is the same as the form authentication with some extra steps, including a toggle to remember the user's session.
