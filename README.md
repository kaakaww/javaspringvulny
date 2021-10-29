# A Vulnerable Spring App

Hi I'm The Vulny Spring App, a modern web stack using the latest(-ish) in Java Spring framework technology.  
I'm both sophisticated and naïve all while using a best in class web framework.

Ever since my developers implemented some new authorization method types and form handlers, I haven't
been feeling very well.

I'm probably due for a thorough checkup using a trusted web application vulnerability scanner.

Will you please scan me?

I recommend [OWASP ZAProxy](https://www.zaproxy.org/) or [StackHawk - Powered by ZAP](https://www.stackhawk.com/)
to make sure all of my bugs are diagnosed.

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

* SQL Injection via search box. - `a%'; insert into item values ((select max(id)+1 from item), 'bad bad description', 'hacker item name'); select * from item where name like  '%banan`
  ' union select case when cast(pg_sleep(5) as varchar) > '' then 0 else 1 end, '1', '1'--
  ' union select 1, cast(pg_sleep(5) as varchar), '1'--
* Cross Site Scripting via search box. - `<script>alert('hey guy');</script>`
