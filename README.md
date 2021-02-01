# Java Spring Vulny

Java Spring Vulny combines the power and sophistication of the Spring framework with some homegrown naïveté to provide a target for web application security test scanners such as [OWASP ZAProxy](https://www.zaproxy.org/) and [StackHawk](https://www.stackhawk.com/).

## Building and Running in Docker

### Build
```shell script
docker-compose build
```

### Run docker
```shell script
docker-compose up -d
```

## Building and Running Without Docker

### Build
```shell script
./gradlew --no-daemon build
```

### Run
```shell script
java -Djava.security.egd=file:/dev/./urandom -jar build/libs/java-spring-vuly-0.1.0.jar
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

## Scanning

A [ZAP](https://www.zaproxy.org/) or [StackHawk](https://www.stackhawk.com/login) scan should uncover these bugs:

| Bug | Example |
| --- | --- |
| SQL Injection via search box | `a%'; insert into item values (999, 'bad bad description', 'hacker item name'); select * from item where name like  '%banan` |
| Cross Site Scripting via search box | `<script>alert('hey guy');</script>` |
