# Java Spring Vulny

Java Spring Vulny combines the power and sophistication of the Spring framework with some homegrown naïveté to provide a platform for testing web application security test scanners such as [OWASP ZAProxy](https://www.zaproxy.org/) and [StackHawk - Powered by ZAP](https://www.stackhawk.com/).

## How to start

### Build
```shell script
docker-compose build
```

### Run docker
```shell script
docker-compose up -d
```

*...or* 

### Build/Run docker
```shell script
docker-compose up --build -d
```

## The diagnosis

A [ZAP]((https://www.zaproxy.org/) or [StackHawk](https://www.stackhawk.com/login) scan should uncover these two nast bugs:

* SQL Injection via search box. - `a%'; insert into item values (999, 'bad bad description', 'hacker item name'); select * from item where name like  '%banan`
* Cross Site Scripting via search box. - `<script>alert('hey guy');</script>`
