const { defineConfig } = require("cypress");
const dotenv = require('dotenv')
const parsedConfig = dotenv.config().parsed

if (!parsedConfig || !parsedConfig["APP_TEST_HOST"]) {
  console.warn("Cannot read .env config or identify scanned host. Cypress wont work.")
}

module.exports = defineConfig({
  env: {
    "LOGIN_PASSWORD": parsedConfig["LOGIN_PASSWORD"],
    "LOGIN_USERNAME": parsedConfig["LOGIN_USERNAME"],
    "TOKEN_NAME": parsedConfig["TOKEN_NAME"],
    "TOKEN_VALUE": parsedConfig["TOKEN_VALUE"]
  },
  e2e: {
    port: 8999,
    experimentalStudio: true,
    baseUrl: parsedConfig["APP_TEST_HOST"],
  },
});
