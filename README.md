# Java Spring Vulny

Java Spring Vulny is a simple application that combines the power and sophistication of the Spring framework with some homegrown naïveté. Its purpose is to provide a target for web application security test scanners such as [OWASP ZAProxy](https://www.zaproxy.org/) and [StackHawk](https://www.stackhawk.com/).

## Building and Running in IDE/commandline
```shell script
# run the postgresql db so you can have SQLi
docker compose up -d db
# run the application in debug mode or run mode with the vm option to activate the profile
# -Dspring.profiles.active=postgresql

./gradlew --no-daemon bootRun --args='--spring.profiles.active=postgresql'
```

## Building and Running in Docker

### Build
```shell script
docker compose build
```

### Run docker
```shell script
docker compose up -d
```

## Building and Running Without Docker

### Build
```shell script
./gradlew --no-daemon build
```

### Run
```shell script
./gradlew --no-daemon bootRun
```

## Building and Running on Windows
```shell script
./gradlew.bat bootRun --args='--spring.profiles.active=windows'
```

### Build

In PowerShell, with administrative privileges:

1. [Install gradle](https://docs.gradle.org/current/userguide/installation.html#microsoft_windows_users)
   1. unpack zip file
   2. add the new gradle directory to `$env:PATH`
2. Run the gradle build:
   1. `.\gradlew.bat --no-daemon build`
3. Update the `spring.datasource.url` in your local [application.yaml](https://github.com/kaakaww/javaspringvulny/blob/main/src/main/resources/application.properties) file from `spring.datasource.url=jdbc:h2:file:${PWD}/db/vulny;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE` to an absolute path.
   1. For instance: `spring.datasource.url: jdbc:h2:file:C:/Users/Dan/projects/javaspringvulny/db/vulny;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE`.

### Run

```shell script
.\gradlew.bat --no-daemon bootRun
```

## Using the Application

### Reaching the App

Once the app starts up, you can reach it at [https://localhost:9000](https://localhost:9000).

### Logging In
You can log in to the application with the following credentials:

```
    username: user
    password: password
```

### URLs of Interest

| URL | Description |
| --- | --- |
| https://localhost:9000 | Home page |
| https://localhost:9000/openapi | The OpenAPI specification for this app |
| https://localhost:9000/openapi.yaml | The OpenAPI spec in YAML format |
| https://localhost:9000/swagger-ui.html | The Swagger doc for the OpenAPI spec |

### Security Bugs

A [ZAP](https://www.zaproxy.org/) or [StackHawk](https://www.stackhawk.com/login) scan should uncover these bugs:

| Bug | Example |
| --- | --- |
| SQL Injection via search box | `a%'; insert into item values (999, 'bad bad description', 'hacker item name'); select * from item where name like  '%banan` |
| Cross Site Scripting via search box | `<script>alert('hey guy');</script>` |

## Scanning

The following examples will run HawkScan against the JavaSpringVulny app running on localhost and port 9000, which is the default setup. The StackHawk configuration files are already present in this repository in the `stackhawk.d` directory.

You should create a new application in the [StackHawk app](https://app.stackhawk.com/applications) to collect data from these scans. The following environment variables are required for these scans to work:

 * `API_KEY`: Your StackHawk [API key](https://app.stackhawk.com/settings/apikeys)
 * `APP_ID`: Your StackHawk [application ID](https://app.stackhawk.com/applications)

For example:

```shell
export API_KEY=<your-StackHawk-API-key>
export APP_ID=<your-StackHawk-App-ID>
```

You can optionally include the following variables to customize the scan.

 * `APP_HOST`: The host to scan. Default: https://localhost:9000
 * `APP_ENV`: The application environment name.

Baseline scan without authentication:
```shell
# With the CLI
hawk scan stackhawk.d/stackhawk.yml

# With Docker
docker run --tty --rm --network host --volume $(pwd):/hawk \
  --env API_KEY \
  --env APP_ID \
  stackhawk/hawkscan stackhawk.d/stackhawk.yml
```

Scan using web form authentication with a session cookie. [See the docs](https://docs.stackhawk.com/hawkscan/authenticated-scanning/form-based-authentication.html#example-form-with-http-parameters-with-cookie-authorization) for more information.
```shell
# With the CLI
hawk scan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-form-cookie.yml

# With Docker
docker run --tty --rm --network host --volume $(pwd):/hawk \
  --env API_KEY \
  --env APP_ID \
  stackhawk/hawkscan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-form-cookie.yml
```

Scan using an authorization token retrieved by POSTing credentials to an API endpoint. [See the docs](https://docs.stackhawk.com/hawkscan/authenticated-scanning/form-based-authentication.html#example-form-with-api-call--json-payload-with-token-authorization) for more information.
```shell
# With the CLI
hawk scan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-json-token.yml

# With Docker
docker run --tty --rm --network host --volume $(pwd):/hawk \
  --env API_KEY \
  --env APP_ID \
  stackhawk/hawkscan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-json-token.yml
```

Scan using an authorization token extracted by an external script. This method can be useful for third-party authentication systems. [See the docs](https://docs.stackhawk.com/hawkscan/authenticated-scanning/inject-cookies-and-tokens.html#injecting-a-token) for more information.
```shell
# With the CLI
hawk scan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-external-token.yml

# With Docker
docker run --tty --rm --network host --volume $(pwd):/hawk \
  --env API_KEY \
  --env APP_ID \
  stackhawk/hawkscan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-external-token.yml
```

Scan using basic authentication, using an external script to derive the correct authorization token. This legacy method is an insecure form of bearer token authentication. [See the docs](https://docs.stackhawk.com/hawkscan/authenticated-scanning/inject-cookies-and-tokens.html#injecting-a-token) for more information.
```shell
# With the CLI
export AUTH_TOKEN=$(./scripts/basic-auth.sh)
hawk scan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-basic.yml

# With Docker
export AUTH_TOKEN=$(./scripts/basic-auth.sh)
docker run --tty --rm --network host --volume $(pwd):/hawk \
  --env API_KEY \
  --env APP_ID \
  --env AUTH_TOKEN \
  stackhawk/hawkscan stackhawk.d/stackhawk.yml stackhawk.d/stackhawk-auth-basic.yml
```

## Examples

Here are examples of how to use HawkScan with differently configured applications and CICD pipelines.

### Example Specs

By default running `hawk scan` will run with the `stackhawk.yml` file in the same directory if it's defined and present, but can instead use named specs such as `hawk scan stackhawk-openapi.yml`. HawkScan also supports layering of multiple specs, such as `hawk scan stackhawk-base.yml stackhawk-windows-custom.yml` for a combined configuration.

Look for these in the [stackhawk.d](https://github.com/kaakaww/javaspringvulny/tree/main/stackhawk.d) directory: 

`stackhawk-openapi.yml` - scan with OpenAPI configuration
`stackhawk-custom-spider-curl.yml` scan with custom discovery using curl
`stackhawk-custom-spider-newman.yml` scan with custom discovery using newman
`stackhawk-auth-script-form-multi.yml` scripted authentication 
`stackhawk-jsv-form-cookie.yml` scan with form authentication and cookie authorization
`stackhawk-jsv-json-token` scan with JSON authentication and token authorization
`stackhawk-ajax.yml` - scan with the ajax spider

### Example Pipelines

These are example CICD pipelines to refer to:

[github actions](https://github.com/kaakaww/javaspringvulny/blob/main/.github/workflows/hawkscan.yml)
[azure-pipelines](https://github.com/kaakaww/javaspringvulny/blob/main/azure-pipelines.yml)
