FROM openjdk:11.0.4-jdk

WORKDIR /app
COPY build/libs/java-spring-vuly-0.1.0.jar .
COPY ./db ./db

ENV PWD=/app
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/java-spring-vuly-0.1.0.jar"]