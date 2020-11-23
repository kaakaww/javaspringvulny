# Java Spring Vulny

Hi I'm Vulny a modern web stack using the latest in Java Spring framework technology.
I'm both sophisticated and naive all while using a best in class in web framework.

You should probably scan me with a web app vulnerability scanner.



### Build
```shell script
docker-compose build
```

### Run docker
```shell script
docker-compose up -d
```

### Do bad stuff
* SQL Injection via search box. - `a%'; insert into item values (999, 'bad bad description', 'hacker item name'); select * from item where name like  '%banan`
* Cross Site Scripting via search box. - `<script>alert('hey guy');</script>`
